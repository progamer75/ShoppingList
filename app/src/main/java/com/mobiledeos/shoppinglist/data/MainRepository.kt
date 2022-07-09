package com.mobiledeos.shoppinglist.data

import android.util.Log
import androidx.lifecycle.LiveData
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
        //ShoppingListRoom.getDatabase(context).shoppingListDao.addList(list)
        list.data.ownerId = checkUserAuth()
        ShoppingListFirestore.addList(list, checkUserAuth())
    }

    fun deleteList(list: ShoppingList) {
        //ShoppingListRoom.getDatabase(context).shoppingListDao.deleteList(list)
        ShoppingListFirestore.deleteList(list)
    }

    fun updateList(list: ShoppingList) {
        //ShoppingListRoom.getDatabase(context).shoppingListDao.updateList(list)
        ShoppingListFirestore.updateList(list)
    }

    /**
     *
     * Возвращает в list список всех shoppingList.
     *
     */
    fun getListsAndSetListener(list: LiveData<MutableList<ShoppingList>>): CollectionReference {
        return ShoppingListFirestore.getListsAndSetListener(checkUserAuth(), list)
    }

    //
    // ShoppingList
    //
    fun addThing(list: ShoppingList, thing: Thing) {
        //ShoppingListRoom.getDatabase(context).shoppingListDao.addThing(thing)
        updateFirestoreThing(list, thing)
    }

    private fun updateFirestoreThing(list: ShoppingList, thing: Thing) {
        ShoppingListFirestore.updateThing(list, thing)
    }

    fun deleteThing(thing: Thing) {
        TODO("DO deleteThing")
        //ShoppingListRoom.getDatabase(context).shoppingListDao.deleteThing(thing)
    }

    fun updateThing(list: ShoppingList, thing: Thing) {
        //ShoppingListRoom.getDatabase(context).shoppingListDao.updateThing(thing)
        updateFirestoreThing(list, thing)
    }

    fun getShoppingList(listId: Int): List<Thing> {
        TODO("DO getShoppingList")
            //ShoppingListRoom.getDatabase(context).shoppingListDao.getThingList(listId)
    }
}
