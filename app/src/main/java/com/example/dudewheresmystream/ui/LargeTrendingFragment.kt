package com.example.dudewheresmystream.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.databinding.FragmentListingBinding

class LargeTrendingFragment: Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentListingBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): LargeTrendingFragment {
            return LargeTrendingFragment()
        }
    }

    private fun initAdapter(binding: FragmentListingBinding): ShowRowAdapter {
        val adapter = ShowRowAdapter(viewModel)
        binding.RVVertical.layoutManager = LinearLayoutManager(activity)
        binding.RVVertical.adapter = adapter

        viewModel.observeTrending().observe(viewLifecycleOwner,
            Observer{ trendingPostList ->
                adapter.submitList(trendingPostList)
                //TODO do we want to lengthen this list or shorten the mini fragment version somehow?
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
        //TODO lets try to get an infinite scroll going
    }

}