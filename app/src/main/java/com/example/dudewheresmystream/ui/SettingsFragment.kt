package com.example.dudewheresmystream.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.dudewheresmystream.R
import com.example.dudewheresmystream.databinding.FragmentSettingsBinding

class SettingsFragment():Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
        private const val miniProvTag = "miniProviderTag"
        private const val miniRegionTag = "miniRegionTag"
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
            setMiniSettings(SettingOption.PROVIDER, miniProvTag)
        }
        binding.regionSetting.setOnClickListener{
            //TODO replace binding.settingsPopup with a new fragment_settings_popup fragment that uses fragment_settings_popup.xml
            //TODO create an adapter for the RV that populates a radiogroup as only one selection is allowed
            setMiniSettings(SettingOption.REGION, miniRegionTag)
        }

        childFragmentManager.addOnBackStackChangedListener {
            if(childFragmentManager.backStackEntryCount == 0){
                closeMiniSettings()
            }
        }
        binding.settingsDimmerView.setOnClickListener{
            childFragmentManager.popBackStack()
        }
    }

    private fun setMiniSettings(option: SettingOption, tag:String){
        childFragmentManager.commit {
            replace(R.id.settingsPopup, SettingsPopupFragment.newInstance(option), tag)
            addToBackStack(tag)
        }
        binding.settingsPopup.isVisible = true
        binding.settingsPopup.isClickable = true
        binding.settingsDimmerView.isVisible = true
        binding.settingsDimmerView.isClickable = true
    }

    private fun closeMiniSettings(){
        binding.settingsPopup.isVisible = false
        binding.settingsPopup.isClickable = false
        binding.settingsDimmerView.isVisible = false
        binding.settingsDimmerView.isClickable = false
    }
}

enum class SettingOption{
    PROVIDER,REGION
}
