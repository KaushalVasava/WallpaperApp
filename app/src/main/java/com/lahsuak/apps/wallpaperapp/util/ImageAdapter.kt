package com.lahsuak.apps.wallpaperapp.util

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lahsuak.apps.wallpaperapp.R
import com.lahsuak.apps.wallpaperapp.model.ImageModel

class ImageAdapter(private val listener: ImageListener) : PagingDataAdapter<ImageModel,
        ImageAdapter.MyViewHolder>(DiffUtilCallBack()) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("TAG", "onBindViewHolder: $item ")
        holder.itemView.setOnClickListener {
            if (item != null)
                listener.onItemClick(item.urls.regular)
        }
        if(item == null){
            Log.d("TAG", "onBindViewHolder: null ")
        }
        else {
            Log.d("TAG", "onBindViewHolder: notnull ")
            Glide.with(holder.itemView)
                .load(item.urls.regular)
                .error(R.drawable.ic_error_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return MyViewHolder(inflater)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
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