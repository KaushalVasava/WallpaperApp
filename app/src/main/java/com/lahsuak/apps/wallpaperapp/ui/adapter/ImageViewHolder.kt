package com.lahsuak.apps.wallpaperapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lahsuak.apps.wallpaperapp.R
import com.lahsuak.apps.wallpaperapp.databinding.ImageItemBinding
import com.lahsuak.apps.wallpaperapp.model.ImageModel

class ImageViewHolder(
    private val binding: ImageItemBinding,
    private val listener: ImageAdapter.ImageListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ImageModel) {
        binding.root.setOnClickListener {
            listener.onItemClick(item.urls.regular)
        }
        Glide.with(binding.imageView.context)
            .load(item.urls.regular)
            .error(R.drawable.ic_error_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageView)
    }
}