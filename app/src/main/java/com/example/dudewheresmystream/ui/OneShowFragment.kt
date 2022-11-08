package com.example.dudewheresmystream.ui

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.api.VideoData
import com.example.dudewheresmystream.databinding.FragmentMinioneshowBinding
import com.example.dudewheresmystream.databinding.FragmentOneshowBinding
import com.example.dudewheresmystream.databinding.FragmentRvHorizontalBinding
import com.example.dudewheresmystream.glide.Glide

class OneShowFragment(private val data: VideoData): Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentOneshowBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(data: VideoData): OneShowFragment {
            return OneShowFragment(data)
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
        Glide.glideFetch(data.thumbnailURL,binding.thumbnail) //TODO should we be caching these or fetching from web each time?
        binding.infoTV.text = data.description //TODO we probably need to add individual elements to handle styling of text such as bolding titles etc.
        binding.title.text = data.title
        initializeFavorite()
        addlinks()
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

    private fun addlinks(){
        //TODO update this once we have live streaming links TextView is inappropriate
        for (item in data.streamingURLs){
            val TV = TextView(this.context)
            TV.text = item
            binding.linkContainer.addView(TV)
        }
    }

}
