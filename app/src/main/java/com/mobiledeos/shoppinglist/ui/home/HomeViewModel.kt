package com.mobiledeos.shoppinglist.ui.home

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.google.firebase.firestore.*
import com.mobiledeos.shoppinglist.data.MainRepository
import com.mobiledeos.shoppinglist.data.ShoppingList
import com.mobiledeos.shoppinglist.data.ShoppingListFL
import com.mobiledeos.shoppinglist.data.Thing
import com.mobiledeos.shoppinglist.forceRefresh
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(application: Application) : AndroidViewModel(application), EventListener<QuerySnapshot> {
    val lists = MutableLiveData<MutableList<ShoppingList>>().apply {
        value = mutableListOf()
    }
    var firestoreListener: ListenerRegistration? = null

    fun startFirestoreListening() {
        viewModelScope.launch {
            MainRepository.fillLists(lists)
        }
        firestoreListener = MainRepository.getListsRef().addSnapshotListener(this)
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
            val data = doc.toObject(ShoppingListFL::class.java)
            when (change.type) {
                DocumentChange.Type.ADDED -> {
                    lists.value?.add(ShoppingList(id, data))
                }
                DocumentChange.Type.MODIFIED -> {
                }
                DocumentChange.Type.REMOVED -> {
                    val sl = lists.value?.find {
                        it.id == id
                    }
                    if(sl != null)
                        lists.value?.remove(sl)
                }
            }
        }
        lists.forceRefresh()
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