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
        Glide.glideIconFetch(data.thumbnailURL!!,binding.thumbnail) //TODO v2 feature: caching
        binding.overviewTV.text = data.description
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
        binding.linkContainerRV.layoutManager = GridLayoutManager(activity,2)
        binding.linkContainerRV.adapter = adapter
        viewModel.observeProviders().observe(viewLifecycleOwner,
            Observer{
                viewModel.scrapeLinks(it.tmdbURL)//posts to StreamData once network request resolves
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
                    binding.originalDateTV.text = "First Air Date: ${it.firstAirDate}"
                }
            })
    }

    private fun setMiniCastView(data: PersonInfo){
        //TODO v2 feature: mini view for person, would need to set up mini frag view in xml layout for largeoneshow see homefragment for reference
        if (data.personType==PersonType.CAST){
            val person = data as CastInfo
        }
        else{
            val person = data as CrewInfo
        }
    }

    override fun onDestroy() {
        viewModel.tmdbDetailClear()
        super.onDestroy()
    }

}
