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


    /** Download image from url without any library
        private fun downloadImage(imageUrl: String): Bitmap?{
            return try {
                val conn = URL(imageUrl).openConnection()
                conn.connect()
                val inputStream = conn.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                bitmap
            }catch (e: Exception){
                Log.e("TAG", "downloadImage: ${e.message}")
                null
            }
        }
    */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        try {
            val item = getItem(position)
            holder.itemView.setOnClickListener {
                if (item != null)
                    listener.onItemClick(item.urls.regular)
            }
            if(item!=null) {
                /** Download image from url without any library

                    CoroutineScope(Dispatchers.IO).launch{
                        val bitmap =  downloadImage(item.urls.regular)
                        withContext(Dispatchers.Main){
                            if(bitmap!=null)
                                holder.imageView.setImageBitmap(bitmap)
                        }
                    }
                 */

                Glide.with(holder.itemView)
                    .load(item.urls.regular)
                    .error(R.drawable.ic_error_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageView)
            }
        }catch(e: Exception){
            Log.d("TAG", "onBindViewHolder: ${e.message} ")
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
