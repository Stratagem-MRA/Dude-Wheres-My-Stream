package com.example.dudewheresmystream.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.dudewheresmystream.databinding.FragmentSettingsBinding

class SettingsFragment():Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO create actual settings w/ ids
        //TODO set on click listeners
        binding.darkModeSwitch.setOnCheckedChangeListener { compoundButton, b ->
            Log.d("","compund.isChecked:${compoundButton.isChecked}\tb val:$b")
            //TODO darkmode stuff
        }
        binding.providerSetting.setOnClickListener {
            //TODO replace binding.settingsPopup with a new fragment_settings_popup fragment that uses fragment_settings_popup.xml
            //TODO create an adapter for the RV that populates with checkboxes as multiple selections are allowed
        }
        binding.regionSetting.setOnClickListener{
            //TODO replace binding.settingsPopup with a new fragment_settings_popup fragment that uses fragment_settings_popup.xml
            //TODO create an adapter for the RV that populates a radiogroup as only one selection is allowed
        }
    }
}
