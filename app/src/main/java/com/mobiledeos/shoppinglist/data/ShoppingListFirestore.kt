package com.mobiledeos.shoppinglist.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "ShoppingListFirestore"

object ShoppingListFirestore {
    private const val TAG = "ShoppingListFirestore"
    private val db = Firebase.firestore

    private fun checkOwnerId(list: ShoppingList) {
        if(list.data.ownerId.isEmpty()) {
            throw ShoppingListException("ownerId is empty", ShoppingListErrorCodes.OwnerIdIsEmpty)
        }
    }

    fun updateList(list: ShoppingList) {
        checkOwnerId(list)
        if (!list.id.isNullOrEmpty())
            db.collection("Lists").document(list.id)
                .set(list.data)
                .addOnFailureListener { e ->
                    throw ShoppingListException(
                        e.message.toString(),
                        ShoppingListErrorCodes.ErrorUpdatingList
                    )
                }
    }

    fun addList(list: ShoppingList, userId: String) {
        checkOwnerId(list)
        db.collection("Lists")
            .add(list.data)
            .addOnSuccessListener {
                list.id = it.id
                addList2User(list, userId)
            }
            .addOnFailureListener { e ->
                throw ShoppingListException(e.message.toString(), ShoppingListErrorCodes.ErrorUpdatingList)
            }
    }

    fun deleteList(list: ShoppingList) {
        TODO("Do deleteList")
    //db.collection("Lists").document(list.id.toString()).delete()
    }

    fun addThing(thing: Thing) {
        db.collection("Lists").document(thing.data.listId).collection("Things")
            .add(thing.data)
            .addOnSuccessListener {
                thing.id = it.id
            }
            .addOnFailureListener { e ->
                throw ShoppingListException(e.message.toString(), ShoppingListErrorCodes.ErrorUpdatingThing)
            }
    }

    fun updateThing(thing: Thing) {
        if(thing.id.isEmpty())
            return
        db.collection("Lists").document(thing.data.listId)
            .collection("Things").document(thing.id)
            .set(thing.data)
            .addOnFailureListener { e ->
                throw ShoppingListException(e.message.toString(), ShoppingListErrorCodes.ErrorUpdatingThing)
            }
    }

    fun deleteThing(thing: Thing) {
        TODO("Do deleteThing")
    }

    fun getListsRef(userId: String): CollectionReference {
        return db.collection("Users").document(userId).collection("Lists")
    }

    suspend fun fillLists(
        userId: String,
        list: MutableLiveData<MutableList<ShoppingList>>) {

        list.value?.clear()
        val dataList = mutableListOf<ShoppingList>()

        val idList = mutableListOf<String>()
        withContext(Dispatchers.IO) {
            getListsRef(userId)
                .get()
                .addOnSuccessListener { docs ->
                    for(doc in docs) {
                        idList.add(doc.id)
                    }

                    if(idList.isEmpty()) {
                        list.postValue(dataList)
                        return@addOnSuccessListener
                    }

                    db.collection("Lists")
                        .whereIn(FieldPath.documentId(), idList)
                        .limit(100) // ограничим количество списков
                        .get()
                        .addOnSuccessListener { docs ->
                            for(doc in docs) {
                                val data = doc.toObject(ShoppingListFL::class.java)
                                dataList.add(ShoppingList(
                                    doc.id,
                                    data
                                ))
                            }
                            list.postValue(dataList)
                        }
                        .addOnFailureListener {
                                ex -> Log.e(TAG, "fillLists error", ex)
                        }
                }
        }
    }

    fun addList2User(list: ShoppingList, userId: String) {
        db.runTransaction { transaction ->
            transaction.set(db.collection("Lists").document(list.id).collection("Users").document(userId),
                mapOf("name" to ""))
            transaction.set(db.collection("Users").document(userId), mapOf("name" to ""))
            transaction.set(db.collection("Users").document(userId).collection("Lists").document(list.id),
                mapOf("name" to ""))
        }
        .addOnFailureListener { e ->
            throw ShoppingListException(
                e.message.toString() + " / ${list.id} / $userId",
                ShoppingListErrorCodes.ErrorAddingUser
            )
        }
    }

    fun getShoppingListRef(listId: String): CollectionReference {
        return db.collection("Lists").document(listId).collection("Things")
    }

    suspend fun fillShoppingList(listId: String, list: MutableLiveData<MutableList<Thing>>) {
        list.value?.clear()
        val dataList = mutableListOf<Thing>()
        withContext(Dispatchers.IO) {
            getShoppingListRef(listId)
                .get()
                .addOnSuccessListener { docs ->
                    for(doc in docs) {
                        val data = doc.toObject(ThingFL::class.java)
                        dataList.add(Thing(doc.id, data))
                    }
                    list.postValue(dataList)
                }
                .addOnFailureListener {
                        ex -> Log.e(TAG, "fillShoppingList error", ex)
                }
        }
    }
}