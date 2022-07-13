package com.mobiledeos.shoppinglist.ui.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiledeos.shoppinglist.databinding.FragmentShoppingListBinding
import com.mobiledeos.shoppinglist.ui.home.detail.ListDataFragmentArgs

private const val TAG = "ShoppingListFragment"

class ShoppingListFragment : Fragment() {
    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!
    lateinit var shoppingListViewModel: ShoppingListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args: ShoppingListFragmentArgs by navArgs()
        val shoppingList = args.shoppingList

        val application = requireNotNull(activity).application
        val viewModelFactory = ShoppingListViewModelFactory(shoppingList, application)

        shoppingListViewModel =
            ViewModelProvider(this, viewModelFactory)[ShoppingListViewModel::class.java]

        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        binding.vm = shoppingListViewModel
        val root: View = binding.root
        binding.lifecycleOwner = this

        val listAdapter = ShoppingListAdapter()
        val listRV = binding.listRecyclerView
        listRV.adapter = listAdapter
        listRV.layoutManager = LinearLayoutManager(activity)
        //listsRV.addItemDecoration(ListsItemDecoration())

        shoppingListViewModel.list.observe(viewLifecycleOwner, Observer {
            it?.let{
                listAdapter.submitList(it)
            }
        })

        return root
    }

    override fun onStart() {
        super.onStart()
        shoppingListViewModel.startFirestoreListening()
    }

    override fun onStop() {
        super.onStop()
        shoppingListViewModel.stopFirestoreListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}