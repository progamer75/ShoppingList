package com.mobiledeos.shoppinglist.ui

interface InteractionListeners<T> {
    fun onClick(a: T)
    fun onLongClick(a: T): Boolean
}