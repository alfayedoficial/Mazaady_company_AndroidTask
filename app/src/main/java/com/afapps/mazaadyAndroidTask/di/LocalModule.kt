package com.afapps.mazaadyAndroidTask.di

import android.content.Context
import androidx.room.Room
import com.afapps.mazaadyAndroidTask.data.local.database.AppDatabase
import com.afapps.mazaadyAndroidTask.utilities.AppConstants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .allowMainThreadQueries()
            .build()
    }


    @Singleton
    @Provides
    fun provideMovieDao(appDatabase: AppDatabase) = appDatabase.movieDao()


}
