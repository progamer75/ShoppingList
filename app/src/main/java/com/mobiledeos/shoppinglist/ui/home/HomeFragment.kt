package com.mobiledeos.shoppinglist.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiledeos.shoppinglist.R
import com.mobiledeos.shoppinglist.ui.InteractionListeners
import com.mobiledeos.shoppinglist.data.ShoppingList
import com.mobiledeos.shoppinglist.databinding.FragmentHomeBinding

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), MenuProvider, InteractionListeners<ShoppingList> {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var homeViewModel: HomeViewModel
    private var editMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        val viewModelFactory = HomeViewModelFactory(application)

        homeViewModel =
            ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.frag = this
        val root: View = binding.root
        binding.lifecycleOwner = this

        val listsAdapter = ListsAdapter(this)
        val listsRV = binding.listsRecyclerView
        listsRV.adapter = listsAdapter
        listsRV.layoutManager = LinearLayoutManager(activity)
        //listsRV.addItemDecoration(ListsItemDecoration())

        homeViewModel.lists.observe(viewLifecycleOwner, Observer {
            it?.let{
                listsAdapter.submitList(it)
            }
        })

        return root
    }

    fun onAddButton(view: View) {
        val action = HomeFragmentDirections.actionNavHomeToNavListData(null, true)
        view.findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        editMode = false
        homeViewModel.startFirestoreListening()
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.stopFirestoreListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(list: ShoppingList) {
        val action = HomeFragmentDirections.actionNavHomeToNavShoppingList(list)
        findNavController().navigate(action)
    }

    override fun onLongClick(list: ShoppingList): Boolean {
        setEditMode(true)
        return true
    }

    private fun setEditMode(mode: Boolean) {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_data) {
        }
        return false
    }
}