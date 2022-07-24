package com.mobiledeos.shoppinglist.ui.shoppinglist.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobiledeos.shoppinglist.data.*

class ThingDataViewModel(private val _thing: Thing?) : ViewModel() {
    val done = MutableLiveData<Boolean>().apply {
        value = false
    }

    var thing = MutableLiveData<Thing>().apply {
        value = _thing
    }

    fun addList() {
        MainRepository.addThing(thing.value!!)
        done.value = true
    }

    fun updateList() {
        MainRepository.updateThing(_thing!!)
        done.value = true
    }
}

class ThingViewModelFactory(private val thing: Thing?) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThingDataViewModel::class.java)) {
            return ThingDataViewModel(thing) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}