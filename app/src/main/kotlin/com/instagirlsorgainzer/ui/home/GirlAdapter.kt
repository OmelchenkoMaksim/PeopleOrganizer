package com.instagirlsorgainzer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.instagirlsorgainzer.databinding.GirlItemBinding
import com.instagirlsorgainzer.room.GirlEntity

class GirlAdapter(private val onGirlClick: (GirlEntity) -> Unit) :
    ListAdapter<GirlEntity, GirlAdapter.GirlViewHolder>(GirlDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GirlViewHolder {
        val binding = GirlItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GirlViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GirlViewHolder, position: Int) {
        val girl = getItem(position)
        holder.bind(girl)
    }

    inner class GirlViewHolder(private val binding: GirlItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val girl = getItem(position)
                    onGirlClick(girl)
                }
            }
        }

        fun bind(girl: GirlEntity) {
            binding.apply {
                nameTextView.text = girl.name
                photosRecyclerView.adapter = PhotoAdapter()
            }
        }
    }
    class GirlDiffCallback : DiffUtil.ItemCallback<GirlEntity>() {
        override fun areItemsTheSame(oldItem: GirlEntity, newItem: GirlEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GirlEntity, newItem: GirlEntity): Boolean {
            return oldItem == newItem
        }
    }
}