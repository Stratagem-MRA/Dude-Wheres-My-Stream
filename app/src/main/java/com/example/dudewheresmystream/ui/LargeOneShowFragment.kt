package com.example.dudewheresmystream.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.api.*
import com.example.dudewheresmystream.databinding.FragmentOneshowBinding
import com.example.dudewheresmystream.glide.Glide

class LargeOneShowFragment(private val data: DiscoverVideoData): Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentOneshowBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(data: DiscoverVideoData): LargeOneShowFragment {
            return LargeOneShowFragment(data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOneshowBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.glideIconFetch(data.thumbnailURL!!,binding.thumbnail) //TODO should we be caching these or fetching from web each time?
        binding.overviewTV.text = data.description //TODO we probably need to add individual elements to handle styling of text such as bolding titles etc.
        binding.title.text = data.nameOrTitle
        initializeFavorite()
        initializeProviderRV()
        initializeCastCrewRV()
        initializeObservers()
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

    private fun initializeProviderRV(){
        val adapter = StreamProviderAdapter(viewModel)
        binding.linkContainerRV.layoutManager = GridLayoutManager(activity,2)//TODO do we like the grid layout manager?
        binding.linkContainerRV.adapter = adapter
        viewModel.observeProviders().observe(viewLifecycleOwner,
            Observer{
                viewModel.scrapeLinks(it.tmdbURL)//posts to StreamData once network request resolves//TODO does this work even with the network call to fetch this data
            })
        viewModel.observeStreamData().observe(viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
            })


        adapter.setOnItemClickListener {
            val i = Intent.parseUri(it.streamURL, Intent.URI_INTENT_SCHEME)
            startActivity(i)
        }
    }
    private fun initializeCastCrewRV(){
        val adapter = PersonColumnAdapter(viewModel)
        binding.personRV.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.personRV.adapter = adapter
        viewModel.observeCredits().observe(viewLifecycleOwner,
            Observer {
                val cast = it.cast
                val crew = it.crew
                adapter.submitList(cast+crew)
            })
        adapter.setOnItemClickListener { setMiniCastView(it) }
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
    }

    private fun setMiniCastView(data: PersonInfo){
        if (data.personType==PersonType.CAST){
            val person = data as CastInfo
            //TODO launch a mini fragment with info on this person
        }
        else{
            val person = data as CrewInfo
            //TODO launch a mini fragment with info on this person
            //TODO set up mini frag view in xml layout for largeoneshow see homefragment for reference
        }
    }

    override fun onDestroy() {
        viewModel.tmdbDetailClear()
        super.onDestroy()
    }

}
