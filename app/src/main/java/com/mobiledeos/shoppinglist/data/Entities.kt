package com.mobiledeos.shoppinglist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class ShoppingList (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String = "",
    val shared: Boolean = false
)

@Entity
data class Users (
    val id: String = "", // предполагается здесь будет номер телефона, может еще чего
    val name: String
) {
    fun getIdHash(): Int { // в firestore будет записываться это вместо id
        TODO("Придумать хэш функцию")
    }
}

@Entity
data class Thing ( //элемент списка покупок
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serial: Int = 0, // порядковый номер в списке
    val listId: Int = 0, // shoppingList.id
    val name: String,
    val category: String = "",
    val quantity: Double = 0.0,
    val unit: String = ""
)

@Entity
data class Categories (
    val order: Int = 0, // порядковый номер в списке категорий
    val name: String,
    val icon: Int = 0 // иконка категории, на будущее
)
