package com.demo.fluid.framework

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.demo.fluid.R
import dagger.hilt.android.AndroidEntryPoint
import com.demo.fluid.framework.presentation.common.LoadingDialog
import com.demo.fluid.framework.presentation.common.lifecycleCallback.FragmentLifecycleCallbacksImpl
import com.demo.fluid.util.Constant
import pion.datlt.libads.AdsController
import pion.tech.fluid_wallpaper.util.PrefUtil
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacksImpl(), true)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main) //TODO: change name navhost
        initAds()
        initActionLanguage()
    }

    fun showLoading() {
        LoadingDialog.getInstance(this)?.show()
    }

    fun hiddenLoading() {
        LoadingDialog.getInstance(this)?.hidden()
    }


    fun currentLanguage(): String {
        return if (AppCompatDelegate.getApplicationLocales()[0] != null) {
            AppCompatDelegate.getApplicationLocales()[0]?.language.toString()
        } else {
            Locale.getDefault().language
        }
    }

    fun goToOnBoard() {
        if (PrefUtil.isChangeLanguageFromFirstOpen) {

        }
    }

    fun setLocale(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }
    private fun initActionLanguage() {
        if (PrefUtil.isChangeLanguageFromSetting) {
            Constant.isSkipLanguageAfterSplash = true
            Constant.isBlockNativeLanguage = true
            PrefUtil.isChangeLanguageFromSetting = false
            getNavHost().popBackStack(R.id.splashFragment, false)
        } else if (PrefUtil.isChangeLanguageFromFirstOpen) {
            //do nothing
            Constant.isBlockNativeLanguage = true
            PrefUtil.isChangeLanguageFromFirstOpen = false
            getNavHost().navigate(R.id.onboardingFragment)
        } else {
            //do nothing
        }
    }
    private fun initAds() {

        AdsController.init(
            activity = this,
            isDebug = false,
            listAppId = arrayListOf(
                getString(R.string.admob_app_id)
            ),
            packageName = packageName,
            navController = getNavHost()
        )
//        val admobDataJson = if (remoteConfig.getString("admob_id").isEmpty()) {
//            CommonUtils.getStringAssetFile("admob_id.json", this)
//        } else {
//            remoteConfig.getString("admob_id")
//        }
//
//        AdsController.init(
//            activity = this,
//            isDebug = false,
//            listAppId = arrayListOf(
//                getString(R.string.admob_app_id)
//            ),
//            packageName = packageName,
//            navController = getNavHost(),
//            listJsonData = arrayListOf(
//                admobDataJson
//            )
//        )
//
//        AdsController.getInstance().initResumeAds(
//            lifecycle = lifecycle,
//            listSpaceName = listOf("appresume1", "appresume2", "appresume3"),
//            onShowOpenApp = {
//                findViewById<TextView>(R.id.viewShowOpenApp).show()
//            },
//            onStartToShowOpenAds = {
//                findViewById<TextView>(R.id.viewShowOpenApp).show()
//            },
//            onCloseOpenApp = {
//                findViewById<TextView>(R.id.viewShowOpenApp).gone()
//            },
//            onPaidEvent = {
//                //do nothing
//            })

    }

    private fun getNavHost(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerMain) as NavHostFragment
        return navHostFragment.navController
    }


}