package com.mobiledeos.shoppinglist.ui.home.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.mobiledeos.shoppinglist.R
import com.mobiledeos.shoppinglist.data.*
import com.mobiledeos.shoppinglist.databinding.FragmentListDataBinding
import com.mobiledeos.shoppinglist.ui.shoppinglist.detail.ThingViewModelFactory

class ListDataFragment : Fragment(), MenuProvider {
    private var _binding: FragmentListDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ListDataViewModel
    private val args: ListDataFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var list = args.list
        if(args.newList)
            list = ShoppingList("", ShoppingListFL())
        val viewModelFactory = ListViewModelFactory(list)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[ListDataViewModel::class.java]

        _binding = FragmentListDataBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.done.observe(viewLifecycleOwner, Observer {
            if(it)
                NavHostFragment.findNavController(this).navigateUp()//popBackStack()
        })

/*
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(binding.listDataToolbar, navHostFragment)

        (activity as AppCompatActivity).setSupportActionBar(binding.listDataToolbar)
*/

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.list_data_menu, menu)
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_data) {
            if(args.newList)
                viewModel.addList()
            else
                viewModel.updateList()
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        if(args.newList) {
            viewModel.setName("New list")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}