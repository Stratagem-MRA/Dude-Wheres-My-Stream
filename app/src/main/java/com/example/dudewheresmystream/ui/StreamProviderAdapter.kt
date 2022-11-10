package com.example.dudewheresmystream.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dudewheresmystream.Jsoup.OneStreamData
import com.example.dudewheresmystream.databinding.StreamLinkBinding
import com.example.dudewheresmystream.glide.Glide

class StreamProviderAdapter(private val viewModel: MainViewModel)
    :ListAdapter<OneStreamData, StreamProviderAdapter.VH>(ProviderDiff()){

    private var listener: ((OneStreamData) -> Unit)? = null

    inner class VH(val streamLinkBinding: StreamLinkBinding)
        :RecyclerView.ViewHolder(streamLinkBinding.root){
            init{
                streamLinkBinding.root.setOnClickListener {
                    listener?.invoke(currentList[adapterPosition])
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val streamLinkBinding = StreamLinkBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(streamLinkBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.streamLinkBinding
        currentList[position].let{
            Glide.glideFetch(it.streamIconURL,binding.streamProviderThumbnail)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun setOnItemClickListener(f: (OneStreamData) -> Unit){
        listener = f
    }

    class ProviderDiff: DiffUtil.ItemCallback<OneStreamData>(){
        override fun areItemsTheSame(oldItem: OneStreamData, newItem: OneStreamData): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: OneStreamData, newItem: OneStreamData): Boolean {
            return oldItem.streamURL == newItem.streamURL
        }
    }
}