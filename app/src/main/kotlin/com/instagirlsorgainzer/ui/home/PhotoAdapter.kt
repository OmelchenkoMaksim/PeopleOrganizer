package com.instagirlsorgainzer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.instagirlsorgainzer.databinding.PhotoItemBinding

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val photos = mutableListOf<String>()

    fun submitList(newPhotos: List<String>) {
        photos.clear()
        photos.addAll(newPhotos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoUrl = photos[position]
        holder.bind(photoUrl)
    }

    override fun getItemCount(): Int = photos.size

    inner class PhotoViewHolder(private val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photoUrl: String) {
            Glide.with(binding.root)
                .load(photoUrl)
                .into(binding.photoImageView)
        }
    }
}