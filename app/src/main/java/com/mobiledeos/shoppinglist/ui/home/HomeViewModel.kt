package com.mobiledeos.shoppinglist.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.mobiledeos.shoppinglist.data.Repository
import com.mobiledeos.shoppinglist.data.ShoppingList
import kotlinx.coroutines.launch

const val TAG = "HomeViewModel"

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _lists = MutableLiveData<List<ShoppingList>>()
    val lists: LiveData<List<ShoppingList>> = _lists

    private val context = application

    init {
        refreshList()
    }

    fun refreshList() {
        viewModelScope.launch {
            _lists.value = Repository.getLists(context)
        }
    }

    fun onAddButton() {
        viewModelScope.launch {
            val sl = Repository.addList(context, "Новый список")
        }
        refreshList() //может быть надо будет добавить LiveData в ShoppingListRoom чтобы не обновлять
    }
}

class HomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}