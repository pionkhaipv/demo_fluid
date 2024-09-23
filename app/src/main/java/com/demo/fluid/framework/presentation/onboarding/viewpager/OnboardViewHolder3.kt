//package pion.tech.camera_detector.framework.presentation.onboarding.viewpager
//
//import android.graphics.Color
//import pion.datlt.libads.utils.adsuntils.showLoadedNative
//import pion.tech.camera_detector.databinding.PagerOnboardView3Binding
//import com.demo.fluid.framework.presentation.onboarding.OnboardingFragment
//import com.demo.fluid.framework.presentation.onboarding.adapter.OnboardingViewpagerAdapter
//import com.demo.fluid.framework.presentation.onboarding.onNextViewPagerEvent
//import com.demo.fluid.framework.presentation.onboarding.onPreviousViewPagerEvent
//import pion.tech.camera_detector.util.changeTextColor
//import pion.tech.camera_detector.util.setPreventDoubleClickScaleView
//
//class OnboardViewHolder3(val binding: PagerOnboardView3Binding, val fragment: OnboardingFragment) :
//    OnboardingViewpagerAdapter.ViewHolder(binding.root) {
//
//    override fun bind() {
//        initView()
//        showAdsNative()
//        nextEvent()
//        previousEvent()
//        chooseLocateEvent()
//    }
//
//    enum class LocateChosen {
//        None, Bedroom, Bathroom, LivingRoom, ClothesRoom
//    }
//
//    private var currentLocateChosen: LocateChosen = LocateChosen.None
//
//    private fun initView() {
//        runCatching {
//            binding.tvDes.changeTextColor(start = 0, end = 17, color = Color.parseColor("#FFD656"))
//        }
//    }
//
//    private fun chooseLocateEvent() {
//        binding.btnBedRoom.setPreventDoubleClickScaleView {
//            currentLocateChosen = LocateChosen.Bedroom
//            setUpUiForChosenLocate()
//            fragment.onNextViewPagerEvent()
//        }
//        binding.btnBathRoom.setPreventDoubleClickScaleView {
//            currentLocateChosen = LocateChosen.Bathroom
//            setUpUiForChosenLocate()
//            fragment.onNextViewPagerEvent()
//        }
//        binding.btnLivingRoom.setPreventDoubleClickScaleView {
//            currentLocateChosen = LocateChosen.LivingRoom
//            setUpUiForChosenLocate()
//            fragment.onNextViewPagerEvent()
//        }
//        binding.btnClothesRoom.setPreventDoubleClickScaleView {
//            currentLocateChosen = LocateChosen.ClothesRoom
//            setUpUiForChosenLocate()
//            fragment.onNextViewPagerEvent()
//        }
//    }
//
//    private fun setUpUiForChosenLocate() {
//        binding.ivBedRoom.borderColor = Color.TRANSPARENT
//        binding.ivBathRoom.borderColor = Color.TRANSPARENT
//        binding.ivLivingRoom.borderColor = Color.TRANSPARENT
//        binding.ivClothesRoom.borderColor = Color.TRANSPARENT
//
//        binding.tvBedRoom.setTextColor(Color.WHITE)
//        binding.tvBathRoom.setTextColor(Color.WHITE)
//        binding.tvLivingRoom.setTextColor(Color.WHITE)
//        binding.tvClothesRoom.setTextColor(Color.WHITE)
//
//        when (currentLocateChosen) {
//            LocateChosen.None -> {
//
//            }
//
//            LocateChosen.Bedroom -> {
//                binding.ivBedRoom.borderColor = Color.parseColor("#2F80ED")
//                binding.ivBedRoom.borderWidth = 5f
//                binding.tvBedRoom.setTextColor(Color.parseColor("#2F80ED"))
//            }
//
//            LocateChosen.Bathroom -> {
//                binding.ivBathRoom.borderColor = Color.parseColor("#2F80ED")
//                binding.ivBathRoom.borderWidth = 5f
//                binding.tvBathRoom.setTextColor(Color.parseColor("#2F80ED"))
//            }
//
//            LocateChosen.LivingRoom -> {
//                binding.ivLivingRoom.borderColor = Color.parseColor("#2F80ED")
//                binding.ivLivingRoom.borderWidth = 5f
//                binding.tvLivingRoom.setTextColor(Color.parseColor("#2F80ED"))
//            }
//
//            LocateChosen.ClothesRoom -> {
//                binding.ivClothesRoom.borderColor = Color.parseColor("#2F80ED")
//                binding.ivClothesRoom.borderWidth = 5f
//                binding.tvClothesRoom.setTextColor(Color.parseColor("#2F80ED"))
//            }
//        }
//    }
//
//    private fun nextEvent() {
//        binding.ivNext.setPreventDoubleClickScaleView {
//            fragment.onNextViewPagerEvent()
//        }
//    }
//
//    private fun previousEvent() {
//        binding.ivPrevious.setPreventDoubleClickScaleView {
//            fragment.onPreviousViewPagerEvent()
//        }
//    }
//
//    private fun showAdsNative() {
//        fragment.showLoadedNative(
//            spaceNameConfig = "Onboard3",
//            spaceName = "onboard2_native",
//            layoutToAttachAds = binding.adViewGroup,
//            layoutContainAds = binding.layoutAds,
//            onAdsClick = {})
//
//    }
//
//    override fun showAdsReload() {
//    }
//
//}