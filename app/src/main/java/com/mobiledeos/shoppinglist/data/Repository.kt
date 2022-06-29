package com.mobiledeos.shoppinglist.data

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG

object Repository {
    // Если список не расшарен, то вся логика в Room,
    // если расшарен, то все происходит в firestore, возможно даже без синхронизации с Room

    /**
     *
     * Создает ShoppingList.
     *
     * @return возвращает ShoppingList или null, если возникла ошибка.
     */
    suspend fun addList(context: Context, list: ShoppingList) {
        try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.addList(list)
            updateFirestoreList(list)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
        }
    }

    private fun updateFirestoreList(list: ShoppingList) {
        ShoppingListFirestore.updateList(list)
    }

    suspend fun deleteList(context: Context, list: ShoppingList) {
        try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.deleteList(list)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
        }
    }

    suspend fun updateList(context: Context, list: ShoppingList) {
        try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.updateList(list)
            updateFirestoreList(list)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
        }
    }

    /**
     *
     * Возвращает список всех shoppingList.
     *
     * @return возвращает список или emptyList, если возникла ошибка.
     */
    suspend fun getLists(context: Context): List<ShoppingList> {
        return try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.getLists()
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
            emptyList()
        }
    }

    //
    // ShoppingList
    //
    suspend fun addThing(context: Context, list: ShoppingList, thing: Thing) {
        try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.addThing(thing)
            updateFirestoreThing(list, thing)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
        }
    }

    private fun updateFirestoreThing(list: ShoppingList, thing: Thing) {
        ShoppingListFirestore.updateThing(list, thing)
    }

    suspend fun deleteThing(context: Context, thing: Thing) {
        try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.deleteThing(thing)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
        }
    }

    suspend fun updateThing(context: Context, list: ShoppingList, thing: Thing) {
        try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.updateThing(thing)
            updateFirestoreThing(list, thing)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
        }
    }

    suspend fun getShoppingList(context: Context, listId: Int): List<Thing> {
        return try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.getThingList(listId)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
            emptyList()
        }
    }
}
