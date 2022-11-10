package com.example.dudewheresmystream.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.databinding.FragmentListingBinding

class SearchFragment():Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentListingBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }


    private fun initAdapter(binding: FragmentListingBinding): ShowRowAdapter {
        val adapter = ShowRowAdapter(viewModel)
        binding.RVVertical.layoutManager = LinearLayoutManager(activity)
        binding.RVVertical.adapter = adapter

        viewModel.observeSearch().observe(viewLifecycleOwner,
            Observer{ searchPostList ->
                adapter.submitList(searchPostList)
                adapter.notifyDataSetChanged()
            })

        adapter.setOnItemClickListener {
            requireActivity().supportFragmentManager.commit {
                replace(R.id.main_frame, LargeOneShowFragment.newInstance(it), "OneShow")
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                addToBackStack("OneShow")
            }
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
        //TODO what is this doing?
    }
}
