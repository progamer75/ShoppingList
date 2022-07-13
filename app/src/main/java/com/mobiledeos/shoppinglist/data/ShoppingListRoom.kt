package com.mobiledeos.shoppinglist.data
/*
import android.content.Context
import androidx.room.*


@Dao
interface ShoppingListDao {
    @Query("Select * from ShoppingList")
    suspend fun getLists(): List<ShoppingList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(list: ShoppingList)

    @Update
    suspend fun updateList(list: ShoppingList)

    @Delete
    suspend fun deleteList(list: ShoppingList)

    @Query("Select * from Thing where listId=:listId")
    suspend fun getThingList(listId: Int): List<Thing>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addThing(thing: Thing)

    @Update
    suspend fun updateThing(thing: Thing)

    @Delete
    suspend fun deleteThing(thing: Thing)

/*
    @Query("Select * from ShoppingListUser Inner join SharedUsers on ShoppingListUser.id=SharedUsers.userId and SharedUsers.listId=:listId")
    suspend fun getSharedUsers(listId: Int): List<ShoppingListUser>
*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(users: ShoppingListUser)

    @Delete
    suspend fun deleteUser(users: ShoppingListUser)
}

@Database(entities = [ShoppingList::class, ShoppingListUser::class, SharedUsers::class, Thing::class, Categories::class], version = 2, exportSchema = false)
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

 */