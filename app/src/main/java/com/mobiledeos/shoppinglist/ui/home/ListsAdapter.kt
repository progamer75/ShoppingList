package com.mobiledeos.shoppinglist.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobiledeos.shoppinglist.ui.InteractionListeners
import com.mobiledeos.shoppinglist.data.ShoppingList
import com.mobiledeos.shoppinglist.databinding.ListRowBinding

class ListsAdapter(private val listeners: InteractionListeners<ShoppingList>):
    ListAdapter<ShoppingList, RecyclerView.ViewHolder>(ShoppingListDiffCallback())
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RowViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RowViewHolder).bind(item, listeners)
    }

    class RowViewHolder private constructor(private val binding: ListRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingList, listeners: InteractionListeners<ShoppingList>) {
            binding.row = item
            binding.listener = listeners
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
        return false//oldItem.data == newItem.data
    }
}


/*    val clickListener: (shoppingList: ShoppingList) -> Unit,
    val longClickListener: (shoppingList: ShoppingList) -> Unit*/
/*    fun onClick(shoppingList: ShoppingList) = clickListener(shoppingList)
    fun onLongClick(shoppingList: ShoppingList) = longClickListener(shoppingList)*/