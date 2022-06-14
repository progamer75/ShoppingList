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
            ShoppingListRoom.getDatabase(context).shoppingListDao.updateList(listId, newName, newDescription)
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
    suspend fun addThing(context: Context, listId: Int,
                         serial: Int,
                         name: String,
                         category: String,
                         quantity: Double,
                         unit: String
        ){
        try {
            val thing = Thing(
                listId = listId,
                name = name,
                serial = serial,
                category = category,
                quantity = quantity,
                unit = unit
                )
            ShoppingListRoom.getDatabase(context).shoppingListDao.addThing(thing)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
        }
    }

    suspend fun deleteThing(context: Context, thing: Thing/*id: Int*/) {
        try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.deleteThing(thing)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, LENGTH_LONG).show()
        }
    }

    suspend fun updateThing(context: Context,
        thing: Thing
/*                            id: Int,
                            newSerial: Int,
                            newName: String,
                            newCategory: String,
                            newQuantity: Double,
                            newUnit: String*/
    ) {
        try {
            ShoppingListRoom.getDatabase(context).shoppingListDao.updateThing(
/*                id,
                newSerial,
                newName,
                newCategory,
                newQuantity,
                newUnit*/
                thing
            )
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
