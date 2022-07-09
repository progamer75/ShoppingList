package com.mobiledeos.shoppinglist.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//@Entity
@Parcelize
data class ShoppingList (
    //@PrimaryKey(autoGenerate = true) val id: Long = 0,
    var id: String,
    val data: ShoppingListFL
) : Parcelable

@Parcelize
data class ShoppingListFL (
    var name: String = "",
    var description: String = "",
    //val shared: Boolean = false,
    var ownerId: String = "",
) : Parcelable

//@Entity
@Parcelize
data class Thing ( //элемент списка покупок
//    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val id: String,
    val order: Int = 0, // порядковый номер в списке
    val listId: Long = 0, // shoppingList.id
    val priority: Int = 1, // 0 - высший (обязательно к покупке), 1 - нормальный, 2 - минимальный
    val name: String,
    val description: String = "",
    val category: String = "",
    val quantity: Double = 0.0,
    val unit: String = ""
) : Parcelable

//@Entity
data class Categories (
//    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val id: String,
    val name: String,
    val order: Int = 0, // порядковый номер в списке категорий
    val icon: Int = 0 // иконка категории, на будущее
)
