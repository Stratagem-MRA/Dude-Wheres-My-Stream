package com.example.dudewheresmystream.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dudewheresmystream.api.SettingData
import com.example.dudewheresmystream.databinding.RadioRowBinding



class RadioAdapter(private val viewModel: MainViewModel)
    : ListAdapter<SettingData,RadioAdapter.VH> {
    inner class VH(val rowBinding:RadioRowBinding): RecyclerView.ViewHolder(rowBinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RadioRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.rowBinding
        currentList[position].let{
            binding.radioTV.text = it.name
            binding.radioTV.hint = it.id
        }
    }
}
