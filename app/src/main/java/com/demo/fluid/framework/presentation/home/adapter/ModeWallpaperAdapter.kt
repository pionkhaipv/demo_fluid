package com.demo.fluid.framework.presentation.home.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.demo.fluid.R
import com.demo.fluid.databinding.LayoutWallpaperMode1Binding
import com.demo.fluid.framework.MainActivity
import eightbitlab.com.blurview.RenderScriptBlur
import com.demo.fluid.util.setPreventDoubleClick
import pion.tech.fluid_wallpaper.util.loadImage

class ModeWallpaperAdapter : RecyclerView.Adapter<ModeWallpaperAdapter.ModeWallPaperViewHolder>() {

    private var activity: MainActivity? = null
    fun setMainActivity(activity: MainActivity) {
        this.activity = activity
    }
    private var listener:Listener ? = null

    fun setListener(listener: Listener){
        this.listener = listener
    }
    interface Listener{
        fun onFluidWallpaperClick()
        fun onTransparentWallPaperClick()
        fun onLiveWallPaperClick()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    var currentPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ModeWallPaperViewHolder {
        return when (viewType) {
            0 -> {
                val viewRoot = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_wallpaper_mode_1, parent, false)
                val binding = LayoutWallpaperMode1Binding.bind(viewRoot)
                Mode1WallPaperViewHolder(binding)
            }

            else -> {
                val viewRoot = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_wallpaper_mode_1, parent, false)
                val binding = LayoutWallpaperMode1Binding.bind(viewRoot)
                Mode2WallPaperViewHolder(binding)
            }
//
//            2 -> {
//                val viewRoot = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.layout_wallpaper_mode_3, parent, false)
//                val binding = LayoutWallpaperMode3Binding.bind(viewRoot)
//                Mode3WallPaperViewHolder(binding)
//            }
//
//            3 -> {
//                val viewRoot = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.layout_wallpaper_mode_4, parent, false)
//                val binding = LayoutWallpaperMode4Binding.bind(viewRoot)
//                Mode4WallPaperViewHolder(binding)
//            }
//
//            else -> {
//                val viewRoot = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.layout_wallpaper_mode_1, parent, false)
//                val binding = LayoutWallpaperMode4Binding.bind(viewRoot)
//                Mode4WallPaperViewHolder(binding)
//            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(
        holder: ModeWallPaperViewHolder, position: Int
    ) {
        holder.bind()
    }


    abstract class ModeWallPaperViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind()

    }

    val blurValue = 0.1f

    inner class Mode1WallPaperViewHolder(private val binding: LayoutWallpaperMode1Binding) :
        ModeWallPaperViewHolder(view = binding.root) {
        override fun bind() {
            binding.ivMain.loadImage(R.drawable.bg_fluid_home)
            binding.root.setPreventDoubleClick {
                listener?.onFluidWallpaperClick()
            }
            if (currentPosition == 0) {
                binding.ivMain.borderColor = Color.parseColor("#9D7E12")
                binding.blurView.isVisible = false
            } else {
                binding.ivMain.borderColor = Color.TRANSPARENT
                binding.blurView.isVisible = true
                val window = activity?.window
                val decorView = window?.decorView
                if (decorView != null) {
                    val rootView: ViewGroup? = decorView.findViewById(android.R.id.content)
                    if (rootView != null) {
                        binding.blurView.setupWith(
                            rootView,
                            RenderScriptBlur(binding.root.context)
                        ) // or RenderEffectBlur
                            .setBlurRadius(blurValue)
                        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
                        binding.blurView.clipToOutline = true
                    }
                }
            }

        }
    }

    inner class Mode2WallPaperViewHolder(private val binding: LayoutWallpaperMode1Binding) :
        ModeWallPaperViewHolder(view = binding.root) {
        override fun bind() {
            binding.ivMain.loadImage(R.drawable.bg_transparent_home)
            binding.root.setPreventDoubleClick {
                listener?.onTransparentWallPaperClick()
            }
            if (currentPosition == 1) {
                binding.ivMain.borderColor = Color.parseColor("#9D7E12")
                binding.blurView.isVisible = false
            } else {
                binding.ivMain.borderColor = Color.TRANSPARENT
                binding.blurView.isVisible = true
                val window = activity?.window
                val decorView = window?.decorView
                if (decorView != null) {
                    val rootView: ViewGroup? = decorView.findViewById(android.R.id.content)
                    if (rootView != null) {
                        binding.blurView.setupWith(
                            rootView,
                            RenderScriptBlur(binding.root.context)
                        ) // or RenderEffectBlur
                            .setBlurRadius(blurValue)
                        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
                        binding.blurView.clipToOutline = true
                    }
                }
            }
        }
    }
}