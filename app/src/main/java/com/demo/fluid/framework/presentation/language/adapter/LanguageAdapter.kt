package com.demo.fluid.framework.presentation.language.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.fluid.R
import com.demo.fluid.databinding.ItemLanguageBinding
import com.demo.fluid.framework.presentation.model.LanguageItem
import com.demo.fluid.framework.presentation.model.getThumbnailUrlByLanguageCode
import pion.tech.fluid_wallpaper.util.loadImage

class LanguageAdapter: ListAdapter<LanguageItem, LanguageAdapter.ViewHolder>(LanguageItemDiffUtil()) {

    var onSelectLanguage: ((LanguageItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLanguageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(languageItem: LanguageItem) {
            if (languageItem.isChecked) {
                binding.clMain.setBackgroundResource(R.drawable.bg_chosen_language)
            } else {
                binding.clMain.setBackgroundResource(R.drawable.bg_unchosen_language)
            }
            binding.txvNameLanguage.text = languageItem.languageName
            binding.root.setOnClickListener {
                onSelectLanguage?.invoke(languageItem)
            }
            binding.ivLogo.loadImage(getThumbnailUrlByLanguageCode(languageItem.languageCode))
        }
    }

}


class LanguageItemDiffUtil : DiffUtil.ItemCallback<LanguageItem>() {

    override fun areItemsTheSame(oldItem: LanguageItem, newItem: LanguageItem): Boolean {
        return oldItem.languageCode == newItem.languageCode
    }

    override fun areContentsTheSame(oldItem: LanguageItem, newItem: LanguageItem): Boolean {
        return oldItem.isChecked == newItem.isChecked
    }

}