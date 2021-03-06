package com.mobiledeos.shoppinglist.ui.shoppinglist

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.*
import com.mobiledeos.shoppinglist.data.MainRepository
import com.mobiledeos.shoppinglist.data.ShoppingList
import com.mobiledeos.shoppinglist.data.Thing
import com.mobiledeos.shoppinglist.data.ThingFL
import com.mobiledeos.shoppinglist.forceRefresh
import kotlinx.coroutines.launch

private const val TAG = "ShoppingListViewModel"

class ShoppingListViewModel(val shoppingList: ShoppingList, application: Application) : AndroidViewModel(application),
    EventListener<QuerySnapshot> {
    val list = MutableLiveData<MutableList<Thing>>().apply {
        value = mutableListOf()
    }
    var firestoreListener: ListenerRegistration? = null

    fun startFirestoreListening() {
        viewModelScope.launch {
            MainRepository.fillShoppingList(shoppingList.id, list)
        }
        firestoreListener = MainRepository.getShoppingListRef(shoppingList.id).addSnapshotListener(this)
    }

    fun stopFirestoreListening() {
        if (firestoreListener != null) {
            firestoreListener!!.remove()
            firestoreListener = null
        }
    }

    override fun onEvent(documentSnapshots: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.e(TAG, error.toString())
            return
        }

        for (change in documentSnapshots!!.documentChanges) {
            val doc = change.document
            val id = doc.id
            val data = doc.toObject(ThingFL::class.java)
            when (change.type) {
                DocumentChange.Type.ADDED -> {
                    list.value?.add(Thing(id, data))
                }
                DocumentChange.Type.MODIFIED -> {
                }
                DocumentChange.Type.REMOVED -> {
                    val thing = list.value?.find {
                        it.id == id
                    }
                    if(thing != null)
                        list.value?.remove(thing)
                }
            }
        }
        Log.i(TAG, "refresh")
        list.forceRefresh()
    }

    fun setCheck(thing: Thing, check: Boolean) {
        thing.data.done = check
        MainRepository.updateThing(thing)
    }
}

class ShoppingListViewModelFactory(val shoppingList: ShoppingList, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            return ShoppingListViewModel(shoppingList, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}