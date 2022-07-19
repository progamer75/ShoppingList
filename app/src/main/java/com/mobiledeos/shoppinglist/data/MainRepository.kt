package com.mobiledeos.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference

private const val TAG = "MainRepository"

object MainRepository {
    private fun checkUserAuth(): String {
        val user = FirebaseAuth.getInstance().currentUser
        if(user == null) {
            throw ShoppingListException("User authentication error", ShoppingListErrorCodes.UserAuthenticationError)
        } else return user.uid
    }

    /**
     *
     * Создает ShoppingList.
     *
     * @return возвращает ShoppingList или null, если возникла ошибка.
     */
    fun addList(list: ShoppingList) {
        list.data.ownerId = checkUserAuth()
        ShoppingListFirestore.addList(list, checkUserAuth())
    }

    fun deleteList(list: ShoppingList) {
        ShoppingListFirestore.deleteList(list)
    }

    fun updateList(list: ShoppingList) {
        ShoppingListFirestore.updateList(list)
    }

    /**
     *
     * Возвращает в list список всех shoppingList.
     *
     */
    suspend fun fillLists(list: MutableLiveData<MutableList<ShoppingList>>) {
        ShoppingListFirestore.fillLists(checkUserAuth(), list)
    }

    fun getListsRef(): CollectionReference {
        return ShoppingListFirestore.getListsRef(checkUserAuth())
    }

    //
    // ShoppingList
    //
    fun addThing(thing: Thing) {
        if(thing.data.listId.isEmpty())
            throw ShoppingListException("addThing error", ShoppingListErrorCodes.ErrorUpdatingThing)
        ShoppingListFirestore.addThing(thing)
    }

    fun updateThing(thing: Thing) {
        if(thing.data.listId.isEmpty())
            throw ShoppingListException("addThing error", ShoppingListErrorCodes.ErrorUpdatingThing)
        ShoppingListFirestore.updateThing(thing)
    }

    fun deleteThing(thing: Thing) {
        TODO("DO deleteThing")
    }

    fun getShoppingListRef(listId: String): CollectionReference {
        return ShoppingListFirestore.getShoppingListRef(listId)
    }

    suspend fun fillShoppingList(listId: String, list: MutableLiveData<MutableList<Thing>>) {
        ShoppingListFirestore.fillShoppingList(listId, list)
    }
}
