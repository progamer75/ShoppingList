package com.mobiledeos.shoppinglist.ui.home

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.google.firebase.firestore.*
import com.mobiledeos.shoppinglist.data.MainRepository
import com.mobiledeos.shoppinglist.data.ShoppingList

private const val TAG = "HomeViewModel"

class HomeViewModel(application: Application) : AndroidViewModel(application), EventListener<QuerySnapshot> {
    val lists = MutableLiveData<MutableList<ShoppingList>>().apply {
        value = mutableListOf()
    }
    var firestoreListener: ListenerRegistration? = null

    init {
        //refreshList()
    }

    fun getListData(): String {
        val a = lists.value
        if(a != null)
            if(a.size > 0)
                return a[1].id
        return "non"
    }

    fun startFirestoreListening() {
        firestoreListener = MainRepository.getListsRef().addSnapshotListener(this)
    }

    fun stopFirestoreListening() {
        if (firestoreListener != null) {
            firestoreListener!!.remove()
            firestoreListener = null
        }
    }

    private fun refreshList() {
/*        viewModelScope.launch {
            _lists.value = MainRepository.getListsAndSetListener()
        }*/
    }

    fun onAddButton(view: View) {
        val action = HomeFragmentDirections.actionNavHomeToNavListData(null, true)
        view.findNavController().navigate(action)
    }

    override fun onEvent(documentSnapshots: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.e(TAG, error.toString())
            return
        }

        MainRepository.fillLists(lists)
/*        for (change in documentSnapshots!!.documentChanges) {
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }*/
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