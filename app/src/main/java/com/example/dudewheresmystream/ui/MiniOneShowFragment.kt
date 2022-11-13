package com.example.dudewheresmystream.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.api.DiscoverVideoData
import com.example.dudewheresmystream.api.ShowType
import com.example.dudewheresmystream.databinding.FragmentMinioneshowBinding
import com.example.dudewheresmystream.glide.Glide

class MiniOneShowFragment(private val data: DiscoverVideoData) : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentMinioneshowBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(data: DiscoverVideoData): MiniOneShowFragment {
            return MiniOneShowFragment(data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMinioneshowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.glideFetch(data.thumbnailURL,binding.thumbnail)
        binding.overviewTV.text = data.description
        binding.title.text = data.nameOrTitle
        binding.seeMoreButton.setOnClickListener { launchSeeMore() }
        initializeFavorite()
        setBackButton()
        initializeObservers()
        initializeRV()
    }

    private fun initializeRV(){
        val adapter = StreamProviderAdapter(viewModel)
        binding.linkContainerRV.layoutManager = GridLayoutManager(activity,2)//TODO do we like the grid layout manager?
        binding.linkContainerRV.adapter = adapter


        viewModel.observeStreamData().observe(viewLifecycleOwner,
        Observer {
            adapter.submitList(it)
        })
        viewModel.observeProviders().observe(viewLifecycleOwner,
            Observer{
                viewModel.scrapeLinks(it.tmdbURL)//posts to StreamData once network request resolves
            })

        adapter.setOnItemClickListener {
            val i = Intent.parseUri(it.streamURL, Intent.URI_INTENT_SCHEME)
            startActivity(i)
        }
    }

    private fun initializeFavorite(){
        var favorite = viewModel.observeFavorites().value!!.contains(data)
        if(favorite){
            binding.favoriteIcon.setImageResource(R.drawable.ic_baseline_star_24)
        }
        else{
            binding.favoriteIcon.setImageResource(R.drawable.ic_baseline_star_outline_24)
        }

        binding.favoriteIcon.setOnClickListener {
            favorite = !favorite
            if(favorite){
                viewModel.postFavorite(data)
                binding.favoriteIcon.setImageResource(R.drawable.ic_baseline_star_24)
            }
            else{
                viewModel.removeFavorite(data)
                binding.favoriteIcon.setImageResource(R.drawable.ic_baseline_star_outline_24)
            }
        }
    }
    private fun setBackButton(){
        requireActivity().onBackPressedDispatcher.addCallback(this){
            for (i: Int in 1..parentFragmentManager.backStackEntryCount) {
                parentFragmentManager.popBackStack()
            }
        }
    }
    private fun launchSeeMore(){
        parentFragmentManager.popBackStack()
        requireActivity().supportFragmentManager.commit {
            replace(R.id.main_frame, LargeOneShowFragment.newInstance(data), "OneShow")
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            addToBackStack("OneShow")
        }
    }
    private fun initializeObservers(){
        viewModel.observeDetails().observe(viewLifecycleOwner,
            Observer {
                if (it.type == ShowType.MOVIE){
                    binding.originalDateTV.text = "Release Date: ${it.releaseDate}"
                }
                else{
                    //TODO this can be ShowType.EMPTY now does that matter?
                    binding.originalDateTV.text = "First Air Date: ${it.firstAirDate}"
                }
            })
        viewModel.observeCredits().observe(viewLifecycleOwner,
            Observer {
                val cast = it.cast
                if(cast.size >=1) {
                    binding.miniCastName1.text = cast[0].name
                    binding.miniCastRole1.text = cast[0].character
                    Glide.glideFetch(cast[0].profilePicURL, binding.miniCastImg1)
                }
                if(cast.size >=2) {
                    binding.miniCastName2.text = cast[1].name
                    binding.miniCastRole2.text = cast[1].character
                    Glide.glideFetch(cast[1].profilePicURL, binding.miniCastImg2)
                }
                if(cast.size >=3) {
                    binding.miniCastName3.text = cast[2].name
                    binding.miniCastRole3.text = cast[2].character
                    Glide.glideFetch(cast[2].profilePicURL, binding.miniCastImg3)
                }
            })
    }

    override fun onDestroy() {
        viewModel.tmdbDetailClear()
        super.onDestroy()
    }
}
