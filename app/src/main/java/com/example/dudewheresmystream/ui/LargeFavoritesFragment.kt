package com.example.dudewheresmystream.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.databinding.FragmentListingBinding

class LargeFavoritesFragment: Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentListingBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): LargeFavoritesFragment {
            return LargeFavoritesFragment()
        }
    }

    private fun initAdapter(binding: FragmentListingBinding): ShowRowAdapter {
        val adapter = ShowRowAdapter(viewModel)
        binding.RVVertical.layoutManager = LinearLayoutManager(activity)
        binding.RVVertical.adapter = adapter

        viewModel.observeFavorites().observe(viewLifecycleOwner,
            Observer{ favoritesPostList ->
                adapter.submitList(favoritesPostList)
                adapter.notifyDataSetChanged()
                //TODO do we want to lengthen this list or shorten the mini fragment version somehow?
            })

        adapter.setOnItemClickListener {
            requireActivity().supportFragmentManager.commit {
                replace(R.id.main_frame, LargeOneShowFragment.newInstance(it), "OneShow")
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                addToBackStack("OneShow")
            }
            viewModel.tmdbDetailRefresh(it)
        }
        return adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvAdapter = initAdapter(binding)
    }

}