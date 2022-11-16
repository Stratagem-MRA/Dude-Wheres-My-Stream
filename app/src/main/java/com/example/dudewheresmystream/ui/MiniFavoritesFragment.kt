package com.example.dudewheresmystream.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dudewheresmystream.databinding.FragmentRvHorizontalBinding

class MiniFavoritesFragment: Fragment() {
    //TODO Note: closing app via back button then relaunching clears the favorites list sometimes. How can we solve this? I think an instructor comment addressed something similar somewhere...
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRvHorizontalBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): MiniFavoritesFragment {
            return MiniFavoritesFragment()
        }
    }

    private fun initAdapter(binding: FragmentRvHorizontalBinding): ShowColumnAdapter {
        val adapter = ShowColumnAdapter(viewModel)
        binding.RVHorizontal.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.RVHorizontal.adapter = adapter

        viewModel.observeFavorites().observe(viewLifecycleOwner,
        Observer{ favoritesPostList ->
            adapter.submitList(favoritesPostList)
            adapter.notifyDataSetChanged()
        })

        adapter.setOnItemClickListener { data ->
            setFragmentResult("displayMiniOneShow", bundleOf("data" to data))
        }
        return adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRvHorizontalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvAdapter = initAdapter(binding)
    }

}