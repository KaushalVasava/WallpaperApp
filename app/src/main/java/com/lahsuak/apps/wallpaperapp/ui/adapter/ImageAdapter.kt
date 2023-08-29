package com.lahsuak.apps.wallpaperapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.lahsuak.apps.wallpaperapp.databinding.ImageItemBinding
import com.lahsuak.apps.wallpaperapp.model.ImageModel

class ImageAdapter(private val listener: ImageListener) :
    PagingDataAdapter<ImageModel, ImageViewHolder>(DiffUtilCallBack()) {
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        return ImageViewHolder(binding, listener)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem.urls.regular == newItem.urls.regular
        }

        override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem.urls.regular == newItem.urls.regular
        }
    }

    interface ImageListener {
        fun onItemClick(url: String)
    }
}
