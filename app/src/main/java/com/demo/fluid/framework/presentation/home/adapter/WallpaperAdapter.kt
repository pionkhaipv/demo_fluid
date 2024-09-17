package com.demo.fluid.framework.presentation.home.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.fluid.databinding.ItemHomeBinding
import com.demo.fluid.framework.presentation.model.HomeModel
import pion.tech.fluid_wallpaper.util.loadImage

class WallpaperAdapter :
    ListAdapter<HomeModel, WallpaperAdapter.ViewHolder>(DiffCallback()) {

    interface Listener {
        fun onItemClick(item: HomeModel)
    }

    private var listener: Listener? = null
    fun setListener(listener: Listener) {
        this.listener = listener
    }

    class DiffCallback : DiffUtil.ItemCallback<HomeModel>() {
        override fun areItemsTheSame(
            oldItem: HomeModel,
            newItem: HomeModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HomeModel,
            newItem: HomeModel
        ): Boolean {
            return false
        }
    }

    inner class ViewHolder(val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeModel) {
            binding.image.loadImage(getPathThumbnail(item.title))
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
        val binding = ItemHomeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }


}