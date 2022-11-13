package com.example.dudewheresmystream.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.api.DiscoverVideoData
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
        Glide.glideFetch(data.thumbnailURL!!,binding.thumbnail) //TODO should we be caching these or fetching from web each time?
        binding.infoTV.text = data.description //TODO we probably need to add individual elements to handle styling of text such as bolding titles etc.
        binding.title.text = data.nameOrTitle
        initializeFavorite()
        initializeRV()
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

    private fun initializeRV(){
        val adapter = StreamProviderAdapter(viewModel)
        binding.linkContainerRV.layoutManager = GridLayoutManager(activity,2)//TODO do we like the grid layout manager?
        binding.linkContainerRV.adapter = adapter
        viewModel.scrapeLinks(data.tmdbURL!!)//posts to StreamData once network request resolves
        viewModel.observeStreamData().observe(viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
            })


        adapter.setOnItemClickListener {
            val i = Intent.parseUri(it.streamURL, Intent.URI_INTENT_SCHEME)
            startActivity(i)
        }
    }

    override fun onDestroy() {
        viewModel.postStreamData(listOf())
        super.onDestroy()
    }

}
