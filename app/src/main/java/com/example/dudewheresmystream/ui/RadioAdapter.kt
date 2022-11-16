package com.example.dudewheresmystream.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dudewheresmystream.api.SettingData
import com.example.dudewheresmystream.databinding.RadioRowBinding



class RadioAdapter(private val viewModel: MainViewModel)
    : ListAdapter<SettingData,RadioAdapter.VH>(RadioDiff()) {
    inner class VH(val rowBinding:RadioRowBinding): RecyclerView.ViewHolder(rowBinding.root){}
    private var currentSelection: SettingData = viewModel.observeRegion().value!! //TODO v2 feature: asking user for their region on starting the app
    private var lastRadioSelected: RadioButton? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RadioRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.rowBinding
        binding.radioTV.text = currentList[position].name

        if (currentList[position].id == currentSelection.id){
            binding.radioButton.isChecked = true
            lastRadioSelected = binding.radioButton
        }
        else{
            binding.radioButton.isChecked = false
        }
        binding.radioButton.setOnClickListener {
            if (lastRadioSelected != binding.radioButton){
                lastRadioSelected?.isChecked = false
            }
            lastRadioSelected = binding.radioButton
            currentSelection = currentList[position]
        }
        binding.radioTV.setOnClickListener {
            if (lastRadioSelected != binding.radioButton){
                lastRadioSelected?.isChecked = false
            }
            binding.radioButton.isChecked = true
            lastRadioSelected = binding.radioButton
            currentSelection = currentList[position]
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class RadioDiff : DiffUtil.ItemCallback<SettingData>(){
        override fun areItemsTheSame(oldItem: SettingData, newItem: SettingData): Boolean {
            return (oldItem.id == newItem.id) && (oldItem.name == newItem.name)
        }

        override fun areContentsTheSame(oldItem: SettingData, newItem: SettingData): Boolean {
            return (oldItem.id == newItem.id) && (oldItem.name == newItem.name)
        }
    }

    fun getCurrentSelection(): SettingData{
        return currentSelection
    }
}
