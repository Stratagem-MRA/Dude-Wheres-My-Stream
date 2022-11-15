package com.example.dudewheresmystream.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dudewheresmystream.databinding.FragmentSettingsPopupBinding

class SettingsPopupFragment(private val option: SettingOption): Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentSettingsPopupBinding? = null
    private val binding get() = _binding!!

    companion object{
        fun newInstance(option: SettingOption): SettingsPopupFragment{
            return SettingsPopupFragment(option)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsPopupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(option){
            SettingOption.PROVIDER ->{
                initProvider()
            }
            SettingOption.REGION ->{
                initRegion()
            }
        }
        setBackButton()
    }

    private fun initProvider(){
        binding.popupTitle.text = "Provider Settings"
        val adapter = RadioAdapter(viewModel)
        binding.popupRV.layoutManager = LinearLayoutManager(context)
        binding.popupRV.adapter = adapter
        //TODO create adapter .kt and .xml probably create an apply button in this existing fragment to confirm changes otherwise keep them as they were before
        //TODO go to viewmodel and set up data for monitoring settings
    }
    private fun initRegion(){
        binding.popupTitle.text = "Region Settings"
        val adapter = CheckBoxAdapter(viewModel)
        binding.popupRV.layoutManager = LinearLayoutManager(context)
        binding.popupRV.adapter = adapter
        //TODO create adapter .kt and .xml probably create an apply button in this existing fragment to confirm changes otherwise keep them as they were before
        //TODO go to viewmodel and set up data for monitoring settings
    }

    private fun setBackButton(){
        requireActivity().onBackPressedDispatcher.addCallback(this){
            for (i: Int in 1..parentFragmentManager.backStackEntryCount) {
                parentFragmentManager.popBackStack()
            }
        }
    }

}
