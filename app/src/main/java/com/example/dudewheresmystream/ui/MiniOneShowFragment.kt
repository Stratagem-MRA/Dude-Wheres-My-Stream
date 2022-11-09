package com.example.dudewheresmystream.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.api.VideoData
import com.example.dudewheresmystream.databinding.FragmentMinioneshowBinding
import com.example.dudewheresmystream.glide.Glide

class MiniOneShowFragment(private val data: VideoData) : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentMinioneshowBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(data: VideoData): MiniOneShowFragment {
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
        binding.infoTV.text = data.description
        binding.title.text = data.title
        binding.seeMoreButton.setOnClickListener { launchSeeMore() }
        initializeFavorite()
        setBackButton()
        addlinks()
    }

    private fun addlinks(){
        //TODO update this once we have live streaming links TextView is inappropriate
        for (item in data.streamingURLs){
            val TV = TextView(this.context)
            TV.text = item
            binding.linkContainer.addView(TV)
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
}
