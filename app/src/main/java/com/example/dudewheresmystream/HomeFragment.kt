package com.example.dudewheresmystream

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commitNow
import com.example.dudewheresmystream.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object{
        fun newInstance(): HomeFragment{
            return HomeFragment()
        }
        private const val trendingFragTag = "trendingFragTag"
        private const val favoritesFragTag = "favoritesFragTag"
    }
    private fun addTrendingFrag(){
        childFragmentManager.commitNow{
            replace(R.id.TrendingShowsFrame, TrendingFragment.newInstance(), HomeFragment.trendingFragTag)
        }
    }
    private fun addFavoritesFrag(){
        childFragmentManager.commitNow{
            replace(R.id.FavoritesFrame, FavoritesFragment.newInstance(), HomeFragment.favoritesFragTag)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTrendingFrag()
        addFavoritesFrag()
        //TODO make sure to test if back button messes up these fragments
        //TODO any other code to run when home fragment launches?
    }
}