package com.mobiledeos.shoppinglist.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingList (
    var id: String,
    val data: ShoppingListFL
) : Parcelable

@Parcelize
data class ShoppingListFL (
    var name: String = "",
    var description: String = "",
    var ownerId: String = "",
    val order: Int = 0 // порядковый номер в списке
) : Parcelable

@Parcelize
data class Thing ( //элемент списка покупок
    var id: String,
    val data: ThingFL
): Parcelable

@Parcelize
data class ThingFL (
    var done: Boolean = false,
    var order: Int = 0, // порядковый номер в списке
    var listId: String = "", // shoppingList.id
    var priority: Int = 1, // 0 - высший (обязательно к покупке), 1 - нормальный, 2 - минимальный
    var name: String = "",
    var description: String = "",
    var category: String = "",
    var quantity: String = "",
    //var unit: String = ""
) : Parcelable

//@Entity
data class Categories (
//    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val id: String,
    val name: String,
    val order: Int = 0, // порядковый номер в списке категорий
    val icon: Int = 0 // иконка категории, на будущее
)
