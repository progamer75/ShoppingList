package com.mobiledeos.shoppinglist.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ShoppingListFirestore {
    private const val TAG = "ShoppingListFirestore"
    private val db = Firebase.firestore

    private fun checkOwnerId(list: ShoppingList) {
        if(list.ownerId.isEmpty()) {
            throw ShoppingListException("ownerId is empty", ShoppingListErrorCodes.OwnerIdIsEmpty)
        }
    }

    fun updateList(list: ShoppingList) { // он же для добавления
        checkOwnerId(list)
        val listData = hashMapOf("name" to list.name,
            "description" to list.description,
            "ownerId" to list.ownerId
        )
        db.collection("Lists").document(list.id.toString())
            .set(listData)
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

    fun addUser(list: ShoppingList, user: ShoppingListUser) {
        checkOwnerId(list)
        val userData = hashMapOf(
            "name" to user.name)
        db.collection("Lists").document(list.id.toString())
            .collection("Users").document(user.id.toString())
            .set(userData)
            .addOnFailureListener { e ->
                throw ShoppingListException(e.message.toString(), ShoppingListErrorCodes.ErrorAddingUser)
            }
    }

    fun deleteUser(user: ShoppingListUser) {
        TODO("Do deleteUser")
    }
}