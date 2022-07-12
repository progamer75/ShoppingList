package com.mobiledeos.shoppinglist.ui.home.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.mobiledeos.shoppinglist.R
import com.mobiledeos.shoppinglist.data.ShoppingListErrorCodes
import com.mobiledeos.shoppinglist.data.ShoppingListException
import com.mobiledeos.shoppinglist.databinding.FragmentListDataBinding

class ListDataFragment : Fragment() {
    private var _binding: FragmentListDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ListDataViewModel
    val args: ListDataFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this)[ListDataViewModel::class.java]

        _binding = FragmentListDataBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.done.observe(viewLifecycleOwner, Observer {
            if(it)
                NavHostFragment.findNavController(this).popBackStack()
        })

/*
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(binding.listDataToolbar, navHostFragment)

        (activity as AppCompatActivity).setSupportActionBar(binding.listDataToolbar)
*/

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_data_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_data) {
            if(args.newList)
                viewModel.addList()
            else
                if(args.list == null)
                    throw ShoppingListException("Update list error", ShoppingListErrorCodes.ErrorUpdatingList)
                else
                    viewModel.updateList(args.list!!.id)
        }

        return super.onOptionsItemSelected(item)
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