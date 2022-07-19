package com.mobiledeos.shoppinglist.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiledeos.shoppinglist.databinding.FragmentHomeBinding

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(activity).application
        val viewModelFactory = HomeViewModelFactory(application)

        homeViewModel =
            ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.vm = homeViewModel
        val root: View = binding.root
        binding.lifecycleOwner = this

        val listsAdapter = ListsAdapter(
            ListsListener { shoppingList ->
                val action = HomeFragmentDirections.actionNavHomeToNavShoppingList(shoppingList)
                findNavController().navigate(action)
            }
        )
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

    override fun onResume() {
        super.onResume()
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
}