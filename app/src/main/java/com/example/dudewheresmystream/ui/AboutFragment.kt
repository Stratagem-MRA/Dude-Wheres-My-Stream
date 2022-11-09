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
import com.example.dudewheresmystream.databinding.FragmentAboutBinding
import com.example.dudewheresmystream.databinding.FragmentListingBinding

class AboutFragment():Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }
}
