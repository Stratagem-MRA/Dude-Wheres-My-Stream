package com.example.dudewheresmystream.ui

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dudewheresmystream.api.DiscoverVideoData
import com.example.dudewheresmystream.databinding.ColumnBinding
import com.example.dudewheresmystream.glide.Glide

class ShowColumnAdapter(private val viewModel: MainViewModel)
    : ListAdapter<DiscoverVideoData, ShowColumnAdapter.VH>(VideoDiff()){

    private var listener: ((DiscoverVideoData) -> Unit)? = null

    //listener setup courtesy of https://stackoverflow.com/questions/57542878/kotlin-recyclerview-start-new-activity
    inner class VH(val columnBinding: ColumnBinding)
        : RecyclerView.ViewHolder(columnBinding.root){
        init{
            columnBinding.root.setOnClickListener {
                listener?.invoke(currentList[adapterPosition])
            }
            columnBinding.columnTV.maxLines = 1
            columnBinding.columnTV.gravity = Gravity.CENTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val columnBinding = ColumnBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return VH(columnBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.columnBinding
        currentList[position].let{
            binding.columnTV.text = it.nameOrTitle
            Glide.glideIconFetch(it.thumbnailURL,binding.columnThumbnail)
        }
    }

    override fun getItemCount() = currentList.size

    fun setOnItemClickListener(f: (DiscoverVideoData) -> Unit){
        listener = f
    }

    class VideoDiff : DiffUtil.ItemCallback<DiscoverVideoData>(){
        override fun areItemsTheSame(oldItem: DiscoverVideoData, newItem: DiscoverVideoData): Boolean {
            return oldItem.description == newItem.description
        }
        override fun areContentsTheSame(oldItem: DiscoverVideoData, newItem: DiscoverVideoData): Boolean {
            return oldItem.description == newItem.description
        }
    }

}