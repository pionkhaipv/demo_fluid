package pion.tech.fluid_wallpaper.framework.presentation.common

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNavHostFragment : NavHostFragment() {

    val fragmentFactory = MainFragmentFactory()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory = fragmentFactory
    }


}