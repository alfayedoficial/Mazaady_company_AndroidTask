package com.afapps.mazaadyAndroidTask.di

import com.afapps.mazaadyAndroidTask.utilities.NetworkConnectionManager
import com.afapps.mazaadyAndroidTask.utilities.NetworkConnectionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AppNetworkModule {
    @Binds
    abstract fun bindNetworkConnectionManager(networkConnectionManagerImpl: NetworkConnectionManagerImpl): NetworkConnectionManager
}