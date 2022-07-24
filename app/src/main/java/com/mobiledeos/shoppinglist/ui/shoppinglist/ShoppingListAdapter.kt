package com.mobiledeos.shoppinglist.ui.shoppinglist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobiledeos.shoppinglist.data.Thing
import com.mobiledeos.shoppinglist.databinding.ThingRowBinding
import com.mobiledeos.shoppinglist.ui.InteractionListeners

class ShoppingListAdapter(
    private val listeners: InteractionListeners<Thing>,
    private val checkedChangeListener: CheckListener,
    ) : ListAdapter<Thing, RecyclerView.ViewHolder>(ThingListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RowViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RowViewHolder).bind(item, listeners, checkedChangeListener)
    }

    class RowViewHolder
        private constructor(private val binding: ThingRowBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(item: Thing, listeners: InteractionListeners<Thing>, checkedChangeListener: CheckListener) {
                binding.row = item
                binding.listener = listeners
                binding.checkedChangeListener = checkedChangeListener
                binding.executePendingBindings()
            }

        companion object {
            fun from(parent: ViewGroup): RowViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ThingRowBinding.inflate(layoutInflater, parent, false)
                return RowViewHolder(binding)
            }
        }
    }
}

class ThingListDiffCallback : DiffUtil.ItemCallback<Thing>() {
    override fun areItemsTheSame(oldItem: Thing, newItem: Thing): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Thing, newItem: Thing): Boolean {
        return false//oldItem.data == newItem.data
    }
}

class CheckListener(
    val checkedChangeListener: (thing: Thing, btn: CompoundButton, check: Boolean) -> Unit
) {
    fun onCheckedChangeListener(thing: Thing, btn: CompoundButton, check: Boolean) = checkedChangeListener(thing, btn, check)
}