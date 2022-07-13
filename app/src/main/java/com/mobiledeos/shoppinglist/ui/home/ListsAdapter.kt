package com.mobiledeos.shoppinglist.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobiledeos.shoppinglist.data.ShoppingList
import com.mobiledeos.shoppinglist.databinding.ListRowBinding

class ListsAdapter(val clickListener: ListsListener): ListAdapter<ShoppingList, RecyclerView.ViewHolder>(ShoppingListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RowViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RowViewHolder).bind(item, clickListener)
    }

    class RowViewHolder private constructor(private val binding: ListRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingList, clickListener: ListsListener) {
            binding.row = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RowViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListRowBinding.inflate(layoutInflater, parent, false)
                return RowViewHolder(binding)
            }
        }
    }
}

class ShoppingListDiffCallback : DiffUtil.ItemCallback<ShoppingList>() {

    override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
        return oldItem == newItem
    }
}

class ListsListener(val clickListener: (shoppingList: ShoppingList) -> Unit) {
    fun onClick(shoppingList: ShoppingList) = clickListener(shoppingList)
}