package com.mobiledeos.shoppinglist

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:text")
fun setText(view: TextView, value: Double) {
    view.text = String.format("%.2f", value)
}