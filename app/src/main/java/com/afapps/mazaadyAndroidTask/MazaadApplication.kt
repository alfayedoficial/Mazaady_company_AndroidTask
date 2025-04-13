package com.afapps.mazaadyAndroidTask

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.request.CachePolicy
import com.afapps.mazaadyAndroidTask.utilities.NetworkConnectionManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MazaadApplication:Application() {

    @Inject
    lateinit var networkConnectionManager: NetworkConnectionManager

    override fun onCreate() {
        super.onCreate()

        val imageLoader = ImageLoader.Builder(this)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()

        Coil.setImageLoader(imageLoader) // set Coil as the default loader

        networkConnectionManager.startListenNetworkState()
    }
}