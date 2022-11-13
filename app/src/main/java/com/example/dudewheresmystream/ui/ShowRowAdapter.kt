package com.example.dudewheresmystream.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dudewheresmystream.api.DiscoverVideoData
import com.example.dudewheresmystream.databinding.RowBinding
import com.example.dudewheresmystream.glide.Glide

class ShowRowAdapter(private val viewModel: MainViewModel)
    : ListAdapter<DiscoverVideoData, ShowRowAdapter.VH>(VideoDiff()){

    private var listener: ((DiscoverVideoData) -> Unit)? = null

    //listener setup courtesy of https://stackoverflow.com/questions/57542878/kotlin-recyclerview-start-new-activity
    inner class VH(val rowBinding: RowBinding)
        : RecyclerView.ViewHolder(rowBinding.root){
        init{
            rowBinding.root.setOnClickListener {
                listener?.invoke(currentList[adapterPosition])
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.rowBinding
        currentList[position].let{
            binding.rowTV.text = it.nameOrTitle
            Glide.glideFetch(it.thumbnailURL!!,binding.rowThumbnail)
            //TODO any other items that need to be set on bind?
        }
    }

    override fun getItemCount() = currentList.size

    fun setOnItemClickListener(f: (DiscoverVideoData) -> Unit){
        listener = f
    }

    class VideoDiff : DiffUtil.ItemCallback<DiscoverVideoData>(){
        override fun areItemsTheSame(oldItem: DiscoverVideoData, newItem: DiscoverVideoData): Boolean {
            return oldItem.description == newItem.description //TODO double check this
        }
        override fun areContentsTheSame(oldItem: DiscoverVideoData, newItem: DiscoverVideoData): Boolean {
            return oldItem.description == newItem.description//TODO double check this
        }
    }

}