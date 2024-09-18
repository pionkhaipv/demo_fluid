package com.demo.fluid.framework.presentation.addTextFluid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.fluid.databinding.ItemFontFamilyBinding
import com.demo.fluid.util.setPreventDoubleClick

class FontFamilyAdapter : ListAdapter<Int, FontFamilyAdapter.ViewHolder>(DiffCallback()) {

    interface Listener {
        fun onItemClick(item: Int)
    }

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    class DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(
            oldItem: Int,
            newItem: Int,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Int,
            newItem: Int,
        ): Boolean {
            return false
        }
    }

    inner class ViewHolder(val binding: ItemFontFamilyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int) {
            val typeface = ResourcesCompat.getFont(binding.root.context, item)
            binding.tvMain.typeface = typeface
            binding.root.setPreventDoubleClick {
                listener?.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFontFamilyBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }


}