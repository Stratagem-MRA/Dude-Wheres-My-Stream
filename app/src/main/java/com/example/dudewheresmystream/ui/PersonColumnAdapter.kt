package com.example.dudewheresmystream.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dudewheresmystream.api.*
import com.example.dudewheresmystream.databinding.ColumnBinding
import com.example.dudewheresmystream.glide.Glide


class PersonColumnAdapter(private val viewModel: MainViewModel)
    : ListAdapter<PersonInfo, PersonColumnAdapter.VH>(PersonDiff()){

    private var listener: ((PersonInfo) -> Unit)? = null

    //listener setup courtesy of https://stackoverflow.com/questions/57542878/kotlin-recyclerview-start-new-activity
    inner class VH(val columnBinding: ColumnBinding)
        : RecyclerView.ViewHolder(columnBinding.root){
        init{
            columnBinding.root.setOnClickListener {
                listener?.invoke(currentList[adapterPosition])
            }
            columnBinding.columnTV.textSize = 13F
            columnBinding.secondaryColumnTV.textSize = 11F
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val columnBinding = ColumnBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(columnBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.columnBinding
        currentList[position].let{
            if(it.personType == PersonType.CAST){
                val person = it as CastInfo
                binding.columnTV.text = person.name
                binding.secondaryColumnTV.text = person.character
                Glide.glidePersonFetch(person.profilePicURL,binding.columnThumbnail)
            }
            else{
                val person = it as CrewInfo
                binding.columnTV.text = person.name
                binding.secondaryColumnTV.text = person.job
                Glide.glidePersonFetch(person.profilePicURL,binding.columnThumbnail)
            }
        }
    }

    override fun getItemCount() = currentList.size

    fun setOnItemClickListener(f: (PersonInfo) -> Unit){
        listener = f
    }

    class PersonDiff : DiffUtil.ItemCallback<PersonInfo>(){
        override fun areItemsTheSame(oldItem: PersonInfo, newItem: PersonInfo): Boolean {
            return if(oldItem.personType != newItem.personType){
                false
            } else{
                if(oldItem.personType == PersonType.CAST){
                    oldItem as CastInfo
                    newItem as CastInfo
                    (oldItem.name == newItem.name) && (oldItem.originalName == newItem.originalName)
                } else{
                    oldItem as CrewInfo
                    newItem as CrewInfo
                    (oldItem.name == newItem.name) && (oldItem.originalName == newItem.originalName)
                }
            }
        }
        override fun areContentsTheSame(oldItem: PersonInfo, newItem: PersonInfo): Boolean {
            return if(oldItem.personType != newItem.personType){
                false
            } else{
                if(oldItem.personType == PersonType.CAST){
                    oldItem as CastInfo
                    newItem as CastInfo
                    (oldItem.name == newItem.name) && (oldItem.originalName == newItem.originalName) && (oldItem.adult==newItem.adult) && (oldItem.castID==newItem.castID) && (oldItem.character == newItem.character) && (oldItem.creditID == newItem.creditID) && (oldItem.department == newItem.department) && (oldItem.gender == newItem.gender) && (oldItem.id == newItem.id) && (oldItem.order == newItem.order) && (oldItem.popularity == newItem.popularity) && (oldItem.profilePicURL == newItem.profilePicURL)
                } else{
                    oldItem as CrewInfo
                    newItem as CrewInfo
                    (oldItem.name == newItem.name) && (oldItem.originalName == newItem.originalName) && (oldItem.adult==newItem.adult) && (oldItem.creditID == newItem.creditID) && (oldItem.department == newItem.department) && (oldItem.gender == newItem.gender) && (oldItem.id == newItem.id) && (oldItem.popularity == newItem.popularity) && (oldItem.profilePicURL == newItem.profilePicURL) && (oldItem.job == newItem.job) && (oldItem.knownForDepartment == newItem.knownForDepartment)
                }
            }
        }
    }

}