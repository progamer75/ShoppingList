package com.mobiledeos.shoppinglist.ui.home.detail

import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobiledeos.shoppinglist.data.MainRepository
import com.mobiledeos.shoppinglist.data.ShoppingList
import com.mobiledeos.shoppinglist.data.ShoppingListFL

class ListDataViewModel : ViewModel() {
    val done = MutableLiveData<Boolean>().apply {
        value = false
    }

    val name = MutableLiveData<String>().apply {
        value = ""
    }

    val description = MutableLiveData<String>().apply {
        value = ""
    }

    fun setName(listName: String) {
        name.value = listName
    }

    fun addList() {
        val list = ShoppingList("", ShoppingListFL(name.value!!, description.value!!))
        MainRepository.addList(list)
        done.value = true
    }

    fun updateList(id: String) {
        val list = ShoppingList(id, ShoppingListFL(name.value!!, description.value!!))
        MainRepository.updateList(list)
        done.value = true
    }
}