package com.mobiledeos.shoppinglist.ui.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiledeos.shoppinglist.R
import com.mobiledeos.shoppinglist.data.ShoppingList
import com.mobiledeos.shoppinglist.data.Thing
import com.mobiledeos.shoppinglist.databinding.FragmentShoppingListBinding
import com.mobiledeos.shoppinglist.ui.InteractionListeners


private const val TAG = "ShoppingListFragment"

class ShoppingListFragment : Fragment(), InteractionListeners<Thing> {
    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!
    lateinit var shoppingListViewModel: ShoppingListViewModel
    lateinit var shoppingList: ShoppingList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args: ShoppingListFragmentArgs by navArgs()
        shoppingList = args.shoppingList

        val application = requireNotNull(activity).application
        val viewModelFactory = ShoppingListViewModelFactory(shoppingList, application)

        shoppingListViewModel =
            ViewModelProvider(this, viewModelFactory)[ShoppingListViewModel::class.java]

        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        binding.frag = this
        val root: View = binding.root
        binding.lifecycleOwner = this

        val listAdapter = ShoppingListAdapter(this,
            CheckListener{ thing, _, check ->
                shoppingListViewModel.setCheck(thing, check)
            }
        )
        val listRV = binding.listRecyclerView
        listRV.adapter = listAdapter
        listRV.layoutManager = LinearLayoutManager(activity)

        shoppingListViewModel.list.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i(TAG, "submit")
                listAdapter.submitList(it)
            }
        })

        return root
    }

    fun onAddButton(view: View) {
        val action = ShoppingListFragmentDirections.actionNavShoppingListToNavThingData(
            shoppingListId = shoppingList.id, thing = null, newThing = true)
        view.findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        shoppingListViewModel.startFirestoreListening()
    }

    override fun onPause() {
        super.onPause()
        shoppingListViewModel.stopFirestoreListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(thing: Thing) {
        val action = ShoppingListFragmentDirections.actionNavShoppingListToNavThingData(
            shoppingListId = shoppingList.id, thing = thing, newThing = false)
        findNavController().navigate(action)
    }

    override fun onLongClick(thing: Thing): Boolean {
        TODO("Not yet implemented")
    }
}