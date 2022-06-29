package com.mobiledeos.shoppinglist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class ShoppingList (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = "",
    val shared: Boolean = false,
    val ownerId: String = ""
)

@Entity(
    primaryKeys = [
        "userId",
        "listId"]
)
data class SharedUsers (
    val userId: String,
    val listId: Long
        )

@Entity
data class ShoppingListUser (
    @PrimaryKey val id: String = "", // предполагается здесь будет номер телефона или еще чего
    val name: String
) {
    fun getIdHash(): Long { // в firestore будет записываться это вместо id
        TODO("Придумать хэш функцию")
    }
}

@Entity
data class Thing ( //элемент списка покупок
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val order: Int = 0, // порядковый номер в списке
    val listId: Long = 0, // shoppingList.id
    val priority: Int = 1, // 0 - высший (обязательно к покупке), 1 - нормальный, 2 - минимальный
    val name: String,
    val description: String = "",
    val category: String = "",
    val quantity: Double = 0.0,
    val unit: String = ""
)

@Entity
data class Categories (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val order: Int = 0, // порядковый номер в списке категорий
    val name: String,
    val icon: Int = 0 // иконка категории, на будущее
)
