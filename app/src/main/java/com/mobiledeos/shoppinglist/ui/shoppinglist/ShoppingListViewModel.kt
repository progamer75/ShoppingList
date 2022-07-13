package com.mobiledeos.shoppinglist.ui.shoppinglist

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.mobiledeos.shoppinglist.data.MainRepository
import com.mobiledeos.shoppinglist.data.ShoppingList
import com.mobiledeos.shoppinglist.data.Thing
import com.mobiledeos.shoppinglist.ui.home.HomeFragmentDirections
import com.mobiledeos.shoppinglist.ui.home.HomeFragmentDirections.Companion.actionNavHomeToNavListData

private const val TAG = "ShoppingListViewModel"

class ShoppingListViewModel(val shoppingList: ShoppingList, application: Application) : AndroidViewModel(application),
    EventListener<QuerySnapshot> {
    val list = MutableLiveData<MutableList<Thing>>().apply {
        value = mutableListOf()
    }
    var firestoreListener: ListenerRegistration? = null

    fun startFirestoreListening() {
        firestoreListener = MainRepository.getShoppingListRef(shoppingList.id).addSnapshotListener(this)
    }

    fun stopFirestoreListening() {
        if (firestoreListener != null) {
            firestoreListener!!.remove()
            firestoreListener = null
        }
    }

    fun onAddButton(view: View) {
/*
        val action = ShoppingListFragmentDirections.actionNavHomeToNavListData(null, true)
        view.findNavController().navigate(action)
*/
    }

    override fun onEvent(documentSnapshots: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.e(TAG, error.toString())
            return
        }

        MainRepository.fillShoppingList(shoppingList.id, list)
/*        for (change in documentSnapshots!!.documentChanges) {
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }*/
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