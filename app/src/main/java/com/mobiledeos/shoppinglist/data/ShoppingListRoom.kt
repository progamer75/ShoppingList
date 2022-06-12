package com.mobiledeos.shoppinglist.data

import android.content.Context
import androidx.room.*

@Dao
interface ShoppingListDao {
    @Query("Select * from ShoppingList")
    suspend fun getShoppingList(): List<ShoppingList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(list: ShoppingList)

    @Query("Delete from ShoppingList where id = :id")
    suspend fun deleteList(id: Int)
}

@Database(entities = [ShoppingList::class, Users::class, Thing::class, Categories::class], version = 1, exportSchema = false)
abstract class ShoppingListRoom: RoomDatabase() {
  abstract val shoppingListDao: ShoppingListDao
    companion object {
        @Volatile
        private var INSTANCE: ShoppingListRoom? = null

        fun getDatabase(context: Context): ShoppingListRoom {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingListRoom::class.java,
                    "shoppinglist_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}