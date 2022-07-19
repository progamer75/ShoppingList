package com.mobiledeos.shoppinglist.ui.shoppinglist.detail

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
import com.mobiledeos.shoppinglist.data.ShoppingListErrorCodes
import com.mobiledeos.shoppinglist.data.ShoppingListException
import com.mobiledeos.shoppinglist.data.Thing
import com.mobiledeos.shoppinglist.data.ThingFL
import com.mobiledeos.shoppinglist.databinding.FragmentListDataBinding
import com.mobiledeos.shoppinglist.databinding.FragmentThingDataBinding
import com.mobiledeos.shoppinglist.ui.home.detail.ListDataFragmentArgs
import com.mobiledeos.shoppinglist.ui.home.detail.ListDataViewModel

class ThingDataFragment : Fragment(), MenuProvider {
    private var _binding: FragmentThingDataBinding? = null
    private val binding get() = _binding!!
    private val args: ThingDataFragmentArgs by navArgs()
    private lateinit var viewModel: ThingDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var thing = args.thing
        if(args.newThing)
            thing = Thing("", ThingFL(name = "", listId = args.shoppingListId))
        val viewModelFactory = ThingViewModelFactory(thing)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[ThingDataViewModel::class.java]

        _binding = FragmentThingDataBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.done.observe(viewLifecycleOwner, Observer {
            if(it)
                NavHostFragment.findNavController(this).navigateUp()//popBackStack()
        })

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.list_data_menu, menu)
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_data) {
            if(args.newThing)
                viewModel.addList()
            else
                viewModel.updateList()
        }

        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}