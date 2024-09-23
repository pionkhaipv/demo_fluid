package com.demo.fluid.framework.presentation.common

import dagger.hilt.android.lifecycle.HiltViewModel
import pion.tech.fluid_wallpaper.framework.presentation.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor() : BaseViewModel() {
    var currentAddedTextFilePath :String? = null
}