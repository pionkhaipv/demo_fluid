package com.demo.fluid.framework.presentation.personal.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.demo.fluid.databinding.ItemFluidBinding
import com.demo.fluid.framework.database.entities.WallpaperWithTextView

class PersonalAdapter : ListAdapter<WallpaperWithTextView, PersonalAdapter.ViewHolder>(DiffCallback()) {

    interface Listener {
        fun onItemClick(item: WallpaperWithTextView)
    }

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    class DiffCallback : DiffUtil.ItemCallback<WallpaperWithTextView>() {
        override fun areItemsTheSame(
            oldItem: WallpaperWithTextView,
            newItem: WallpaperWithTextView
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: WallpaperWithTextView,
            newItem: WallpaperWithTextView
        ): Boolean {
            return false
        }
    }

    inner class ViewHolder(val binding: ItemFluidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WallpaperWithTextView) {
            Glide.with(binding.root.context)
                .load(getPathThumbnail(item.wallpaperName.nameWallpaper))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.llLoading.isVisible = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.llLoading.isVisible = false
                        return false
                    }

                })
                .into(binding.ivMain)
            binding.root.setOnClickListener {
                listener?.onItemClick(item)
            }
        }
    }

    private fun getPathThumbnail(str: String): Uri {
        return Uri.parse("file:///android_asset/image/$str.webp")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFluidBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }


}