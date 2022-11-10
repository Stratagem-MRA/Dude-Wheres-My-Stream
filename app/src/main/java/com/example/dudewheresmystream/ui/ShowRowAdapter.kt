package com.example.dudewheresmystream.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dudewheresmystream.api.VideoData
import com.example.dudewheresmystream.databinding.ColumnBinding
import com.example.dudewheresmystream.databinding.RowBinding
import com.example.dudewheresmystream.glide.Glide

class ShowRowAdapter(private val viewModel: MainViewModel)
    : ListAdapter<VideoData, ShowRowAdapter.VH>(VideoDiff()){

    private var listener: ((VideoData) -> Unit)? = null

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
            binding.rowTV.text = it.title
            Glide.glideFetch(it.thumbnailURL,binding.rowThumbnail)
            //TODO any other items that need to be set on bind?
        }
    }

    override fun getItemCount() = currentList.size

    fun setOnItemClickListener(f: (VideoData) -> Unit){
        listener = f
    }

    class VideoDiff : DiffUtil.ItemCallback<VideoData>(){
        override fun areItemsTheSame(oldItem: VideoData, newItem: VideoData): Boolean {
            return oldItem.key == newItem.key
        }
        override fun areContentsTheSame(oldItem: VideoData, newItem: VideoData): Boolean {
            return VideoData.spannableStringsEqual(oldItem.title,newItem.title) &&
                    VideoData.spannableStringsEqual(oldItem.description,newItem.description)
        }
    }

}