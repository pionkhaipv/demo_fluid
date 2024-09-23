//package com.demo.fluid.framework.presentation.onboarding.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import pion.tech.camera_detector.databinding.PagerOnboardFullNativeBinding
//import pion.tech.camera_detector.databinding.PagerOnboardView1Binding
//import pion.tech.camera_detector.databinding.PagerOnboardView2Binding
//import pion.tech.camera_detector.databinding.PagerOnboardView3Binding
//import pion.tech.camera_detector.databinding.PagerOnboardView4Binding
//import com.demo.fluid.framework.presentation.onboarding.OnboardingFragment
//import pion.tech.camera_detector.framework.presentation.onboarding.viewpager.OnboardFullNativeViewHolder
//import pion.tech.camera_detector.framework.presentation.onboarding.viewpager.OnboardViewHolder1
//import pion.tech.camera_detector.framework.presentation.onboarding.viewpager.OnboardViewHolder2
//import pion.tech.camera_detector.framework.presentation.onboarding.viewpager.OnboardViewHolder3
//import pion.tech.camera_detector.framework.presentation.onboarding.viewpager.OnboardViewHolder4
//
//class OnboardingViewpagerAdapter(val fragment: OnboardingFragment) :
//    RecyclerView.Adapter<OnboardingViewpagerAdapter.ViewHolder>() {
//
//    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        abstract fun bind()
//        abstract fun showAdsReload()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return when (viewType) {
//            0 -> {
//                val binding = PagerOnboardView1Binding.inflate(inflater, parent, false)
//                OnboardViewHolder1(binding, fragment)
//            }
//
//            1 -> {
//                val binding = PagerOnboardView2Binding.inflate(inflater, parent, false)
//                OnboardViewHolder2(binding, fragment)
//            }
//
//            2 -> {
//                val binding = PagerOnboardFullNativeBinding.inflate(inflater, parent, false)
//                OnboardFullNativeViewHolder(binding, fragment)
//            }
//
//            3 -> {
//                val binding = PagerOnboardView3Binding.inflate(inflater, parent, false)
//                OnboardViewHolder3(binding, fragment)
//            }
//
//            else -> {
//                val binding = PagerOnboardView4Binding.inflate(inflater, parent, false)
//                OnboardViewHolder4(binding, fragment)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return 5
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind()
//    }
//}