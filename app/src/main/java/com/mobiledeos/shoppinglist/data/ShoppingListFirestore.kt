package com.mobiledeos.shoppinglist.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    fun updateThing(list: ShoppingList, thing: Thing) { // он же для добавления
        checkOwnerId(list)
        val thingData = hashMapOf(
            "order" to thing.order,
            "listId" to thing.listId,
            "priority" to thing.priority,
            "name" to thing.name,
            "description" to thing.description,
            "category" to thing.category,
            "quantity" to thing.quantity,
            "unit" to thing.unit
        )
        db.collection("Lists").document(list.id.toString())
            .collection("Things").document(thing.id.toString())
            .set(thingData)
            .addOnFailureListener { e ->
                throw ShoppingListException(e.message.toString(), ShoppingListErrorCodes.ErrorUpdatingThing)
            }
    }

    fun deleteThing(thing: Thing) {
        TODO("Do deleteThing")
    }

/*
    fun getListsRef(): CollectionReference {
        return db.collection("Users").document(userId).collection("Lists")
    }
*/

    fun getListsAndSetListener(
        userId: String,
        list: LiveData<MutableList<ShoppingList>>
    ): CollectionReference {
        //val list = mutableListOf<ShoppingList>()
        list.value?.clear()

        val idList = mutableListOf<String>()
        db.collection("Users").document(userId).collection("Lists")
            .get()
            .addOnSuccessListener { docs ->
                for(doc in docs) {
                    idList.add(doc.id)
                }
            }
        if(idList.size > 0)
            db.collection("Lists")
                .whereIn(FieldPath.documentId(), idList)
                .limit(100) // ограничим количество списков
                .get()
                .addOnSuccessListener { docs ->
                    for(doc in docs) {
                        val data = doc.toObject(ShoppingListFL::class.java)
                        list.value?.add(ShoppingList(
                            doc.id,
                            data
                        ))
                    }
                }
                .addOnFailureListener {
                    ex -> Log.e(TAG, "getLists error", ex)
                }
        return db.collection("Users").document(userId).collection("Lists")
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
}