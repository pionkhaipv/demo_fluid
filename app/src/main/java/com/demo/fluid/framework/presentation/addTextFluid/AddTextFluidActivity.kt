package com.demo.fluid.framework.presentation.addTextFluid

import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.demo.fluid.R
import com.demo.fluid.databinding.ActivityAddTextFluidBinding
import com.demo.fluid.framework.presentation.addTextFluid.adapter.ColorAdapter
import com.demo.fluid.framework.presentation.addTextFluid.adapter.FontFamilyAdapter
import com.demo.fluid.util.BundleKey
import com.magicfluids.Config
import com.magicfluids.NativeInterface

class AddTextFluidActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddTextFluidBinding

    var config: Config? = null
    var nameWallpaper: String = "AbstractAdventure"
    var nativeInterface = NativeInterface()

    var currentEditTextView: TextView? = null

    val colorAdapter = ColorAdapter()
    val fontFamilyAdapter = FontFamilyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTextFluidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameWallpaper =
            intent.getStringExtra(BundleKey.KEY_FLUID_NAME_ADD_TEXT) ?: "AbstractAdventure"
        initView()
        setUpSurfaceView()
        onBackEvent()
        addTextEvent()
        setUpCustomTextView()
        onApplyEvent()
        onDoneEvent()
        setUpAdapter()
        onEditTextEvent()
        onDeleteTextEvent()
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
    }
}