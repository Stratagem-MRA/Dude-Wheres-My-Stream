package com.example.dudewheresmystream.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.*
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.api.DiscoverVideoData
import com.example.dudewheresmystream.api.ProvidersVideoData
import com.example.dudewheresmystream.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object{
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
        private const val trendingFragTag = "trendingFragTag"
        private const val favoritesFragTag = "favoritesFragTag"
        private const val miniFragTag = "miniFragTag"
    }
    private fun addTrendingFrag(){
        childFragmentManager.commit{
            replace(R.id.TrendingShowsFrame, MiniTrendingFragment.newInstance(), trendingFragTag)
        }
    }
    private fun addFavoritesFrag(){
        childFragmentManager.commit{
            replace(R.id.FavoritesFrame, MiniFavoritesFragment.newInstance(), favoritesFragTag)
        }
    }
    private fun setMiniOneShow(data: DiscoverVideoData){
        childFragmentManager.commit{
            replace(R.id.MiniOneShowFrame, MiniOneShowFragment.newInstance(data), miniFragTag)
            addToBackStack("miniOneShow")
        }
        binding.MiniOneShowFrame.isVisible = true
        binding.MiniOneShowFrame.isClickable = true
        binding.DimmerView.isClickable = true
        binding.DimmerView.isVisible = true
        viewModel.tmdbDetailRefresh(data)

    }
    private fun closeMiniOneShow(){
        binding.MiniOneShowFrame.isVisible = false
        binding.MiniOneShowFrame.isClickable = false
        binding.DimmerView.isVisible = false
        binding.DimmerView.isClickable = false

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
        viewModel.tmdbTrendingRefresh("1")//TODO possibly add provider/region codes from settings here
        childFragmentManager.setFragmentResultListener("displayMiniOneShow", viewLifecycleOwner){key, bundle ->
            val data = bundle.get("data") as DiscoverVideoData
            setMiniOneShow(data)
        }

        childFragmentManager.addOnBackStackChangedListener {
            if(childFragmentManager.backStackEntryCount == 0){
                closeMiniOneShow()
            }
        }
        binding.DimmerView.setOnClickListener{
            childFragmentManager.popBackStack()
        }
    }

}