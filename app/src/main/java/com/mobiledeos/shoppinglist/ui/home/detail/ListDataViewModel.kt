package com.mobiledeos.shoppinglist.ui.home.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobiledeos.shoppinglist.data.MainRepository
import com.mobiledeos.shoppinglist.data.ShoppingList

class ListDataViewModel(private val _list: ShoppingList?) : ViewModel() {
    val done = MutableLiveData<Boolean>().apply {
        value = false
    }

    var list = MutableLiveData<ShoppingList>().apply {
        value = _list
    }

    fun setName(listName: String) {
        _list?.data?.name = listName
    }

    fun addList() {
        MainRepository.addList(_list!!)
        done.value = true
    }

    fun updateList() {
        MainRepository.updateList(_list!!)
        done.value = true
    }
}

class ListViewModelFactory(private val list: ShoppingList?) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListDataViewModel::class.java)) {
            return ListDataViewModel(list) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}