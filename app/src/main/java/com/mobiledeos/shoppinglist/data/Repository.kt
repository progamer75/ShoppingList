package com.mobiledeos.shoppinglist.data

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG

object Repository {
    // Если список не расшарен, то вся логика в Room,
    // если расшарен, то все происходит в firestore, возможно даже без синхронизации в Room

    /**
     *
     * Создает ShoppingList.
     *
     * @return возвращает ShoppingList или null, если возникла ошибка.
     */
    suspend fun createList(context: Context, listName: String, listDescription: String): ShoppingList? {
        val list = ShoppingList(
            name = listName,
            description = listDescription
        )

        try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.addList(list)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
            return null
        }

        return list
    }

    suspend fun deleteList(context: Context, id: Int) {
        try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.deleteList(id)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
        }
    }

    suspend fun updateList(context: Context, listId: Int, newName: String, newDescription: String) {
        try {
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
        }
    }

    suspend fun getLists(context: Context): List<ShoppingList> {
        TODO("Сделать getLists")
    }

    //
    // ShoppingList
    //
    suspend fun addThing(context: Context, thing: Thing) {
        TODO("Сделать addThing")
    }

    suspend fun deleteThing(context: Context, id: Int) {

    }

    suspend fun updateThing(context: Context, thing: Thing) {

    }

    suspend fun getShoppingList(context: Context, listId: Int): List<ShoppingList> {
        TODO("Do getShoppingList")
    }

}
