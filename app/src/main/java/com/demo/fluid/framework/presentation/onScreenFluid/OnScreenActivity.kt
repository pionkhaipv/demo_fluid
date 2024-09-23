package com.demo.fluid.framework.presentation.onScreenFluid

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.demo.fluid.databinding.ActivityOnScreenBinding
import com.magicfluids.Config
import com.magicfluids.NativeInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnScreenBinding

    var config: Config? = null
    var nameWallpaper: String = "AbstractAdventure"
    var nativeInterface = NativeInterface()

    var randomHandler :Handler? = null
    var randomRunnable:Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nameWallpaper = intent.getStringExtra("nameWallpaper") ?: "AbstractAdventure"
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivityOnScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        onBackEvent()
        randomSwipe()
    }

    override fun onResume() {
        super.onResume()
        binding.surfaceView.onResume()
        nativeInterface.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeInterface.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        randomRunnable?.let { randomHandler?.removeCallbacks(it) }
    }

}