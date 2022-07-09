package com.mobiledeos.shoppinglist.ui.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mobiledeos.shoppinglist.databinding.FragmentShoppingListBinding

class ShoppingListFragment : Fragment() {
    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val shoppingListViewModel =
            ViewModelProvider(this)[ShoppingListViewModel::class.java]

        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        val root: View = binding.root

/*        val textView: TextView = binding.textGallery
        shoppingListViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}