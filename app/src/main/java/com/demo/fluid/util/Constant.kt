package com.demo.fluid.util

import android.widget.TextView
import androidx.lifecycle.MutableLiveData

object Constant {

    var isPremium = false

    const val iapId = "iapremove"

    const val iapGiaGach = "iapgiagach"

    var timeShowDialogChangeLanguage = 4000L

    var isPreloadOnBoardAtLanguage = false
    var isSkipLanguageAfterChange = false
    var isBlockNativeLanguage = false
    var lastTimeShowAdsInter = 0L
    var isSkipLanguageAfterSplash = false

    enum class RemoteConfigState { NONE, LOADING, DONE }

    val remoteConfigState = MutableLiveData(RemoteConfigState.NONE)


    val textViewList = mutableListOf<TextView>()
    var smallFrameWidth: Int = 0
    var smallFrameHeight: Int = 0

}