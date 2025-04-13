package com.afapps.mazaadyAndroidTask.di

import android.content.Context
import android.content.SharedPreferences
import com.afapps.mazaadyAndroidTask.R
import com.afapps.mazaadyAndroidTask.utilities.KUPreferences
import com.afapps.mazaadyAndroidTask.utilities.annotation.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppPreferencesModule {


    @Singleton
    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.app_name) + "1",
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun providesSharedPreferencesEditor(mSharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return mSharedPreferences.edit()
    }

    @Singleton
    @Provides
    fun providesSharedPreferencesHelper(
        sharedPreferences: SharedPreferences,
        sharedPreferencesEditor: SharedPreferences.Editor
    ): KUPreferences {
        return KUPreferences(sharedPreferences, sharedPreferencesEditor)
    }


    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}