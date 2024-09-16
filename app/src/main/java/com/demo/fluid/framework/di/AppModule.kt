package pion.tech.fluid_wallpaper.framework.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.demo.fluid.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pion.tech.fluid_wallpaper.util.PrefUtil


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideGlide(@ApplicationContext context: Context): RequestManager {
        return Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .error(R.drawable.ic_error)
        )
    }

    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(
            "CLEAN_PREFERENCES",
            Context.MODE_PRIVATE
        )
    }

    @Provides
    fun provideSharedPrefsEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    @Provides
    fun providePrefUtil(sharedPreferences: SharedPreferences , editor: Editor): PrefUtil {
        PrefUtil.sharedPreferences = sharedPreferences
        PrefUtil.editor = editor
        return PrefUtil()
    }

}
