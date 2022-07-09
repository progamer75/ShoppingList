package com.mobiledeos.shoppinglist.ui.shoppinglist.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobiledeos.shoppinglist.R
import com.mobiledeos.shoppinglist.databinding.FragmentListDataBinding
import com.mobiledeos.shoppinglist.databinding.FragmentThingDataBinding
import com.mobiledeos.shoppinglist.ui.home.detail.ListDataViewModel

class ThingDataFragment : Fragment() {
    private var _binding: FragmentThingDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel =
            ViewModelProvider(this)[ThingDataViewModel::class.java]

        _binding = FragmentThingDataBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}