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
import com.example.dudewheresmystream.databinding.CheckboxRowBinding
import com.example.dudewheresmystream.databinding.RadioRowBinding



class CheckboxAdapter(private val viewModel: MainViewModel)
    : ListAdapter<SettingData,CheckboxAdapter.VH>(CheckboxDiff()) {
    inner class VH(val rowBinding:CheckboxRowBinding): RecyclerView.ViewHolder(rowBinding.root){}
    private var currentSelections: MutableList<SettingData> = viewModel.observePreferredProviders().value!!.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = CheckboxRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.rowBinding
        binding.checkboxTV.text = currentList[position].name
        binding.checkbox.isChecked = currentList[position] in currentSelections
        binding.checkbox.setOnClickListener {
            if(binding.checkbox.isChecked){
                currentSelections.add(currentList[position])
                Log.d("",currentSelections.toString())
            }
            else{
                currentSelections.remove(currentList[position])
                Log.d("",currentSelections.toString())
            }
        }
        binding.checkboxTV.setOnClickListener {
            if(!binding.checkbox.isChecked){
                currentSelections.add(currentList[position])
                binding.checkbox.isChecked = true
            }
            else{
                currentSelections.remove(currentList[position])
                binding.checkbox.isChecked = false
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class CheckboxDiff : DiffUtil.ItemCallback<SettingData>(){
        override fun areItemsTheSame(oldItem: SettingData, newItem: SettingData): Boolean {
            return (oldItem.id == newItem.id) && (oldItem.name == newItem.name)
        }

        override fun areContentsTheSame(oldItem: SettingData, newItem: SettingData): Boolean {
            return (oldItem.id == newItem.id) && (oldItem.name == newItem.name)
        }
    }

    fun getCurrentSelections(): List<SettingData>{
        return currentSelections
    }
}
