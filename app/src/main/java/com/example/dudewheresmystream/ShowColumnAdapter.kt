package com.example.dudewheresmystream

import android.provider.MediaStore.Video
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dudewheresmystream.api.VideoData
import com.example.dudewheresmystream.databinding.ColumnBinding
import com.example.dudewheresmystream.glide.Glide

class ShowColumnAdapter(private val viewModel: MainViewModel)
    : ListAdapter<VideoData, ShowColumnAdapter.VH>(VideoDiff()){

    private var listener: ((VideoData) -> Unit)? = null

    //listener setup courtesy of https://stackoverflow.com/questions/57542878/kotlin-recyclerview-start-new-activity
    inner class VH(val columnBinding: ColumnBinding)
        : RecyclerView.ViewHolder(columnBinding.root){
        init{
            columnBinding.root.setOnClickListener {
                listener?.invoke(currentList[adapterPosition])
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val columnBinding = ColumnBinding.inflate(LayoutInflater.from(parent.context),parent,false)//TODO still dont know what attach to parent does

        return VH(columnBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.columnBinding
        currentList[position].let{
            binding.columnTV.text = it.title
            Glide.glideFetch(it.thumbnailURL,binding.columnThumbnail)
            //TODO any other items that need to be set on bind?
        }
    }

    override fun getItemCount() = currentList.size

    fun setOnItemClickListener(f: (VideoData) -> Unit){
        listener = f
    }

    class VideoDiff : DiffUtil.ItemCallback<VideoData>(){
        override fun areItemsTheSame(oldItem: VideoData, newItem: VideoData): Boolean {
            return oldItem.title == newItem.title //TODO might need to change this as multiple shows may have the same title???
        }
        override fun areContentsTheSame(oldItem: VideoData, newItem: VideoData): Boolean {
            return VideoData.spannableStringsEqual(oldItem.title,newItem.title) &&
                    VideoData.spannableStringsEqual(oldItem.description,newItem.description)
        }
    }

}