package com.afapps.mazaadyAndroidTask.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import com.afapps.mazaadyAndroidTask.data.local.dao.MovieDao
import com.afapps.mazaadyAndroidTask.data.local.database.AppDatabase
import com.afapps.mazaadyAndroidTask.data.local.entities.MovieEntity
import com.afapps.mazaadyAndroidTask.data.mediator.MoviesRemoteMediator
import com.afapps.mazaadyAndroidTask.data.remote.api.ApiService
import com.afapps.mazaadyAndroidTask.data.repo.MovieRepoImpl
import com.afapps.mazaadyAndroidTask.domain.repo.MovieRepo
import com.afapps.mazaadyAndroidTask.utilities.annotation.BaseURL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do : Hilt RepositoryModule @Inject
 * Date 12/4/2026
 */


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule{

    @Singleton
    @Binds
    fun bindMovieRepo(movieRepoImpl: MovieRepoImpl): MovieRepo

}

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object MediatorModule{

    @Singleton
    @Provides
    fun provideMediator(appDatabase: AppDatabase, apiService: ApiService, @BaseURL baseUrl: String): RemoteMediator<Int, MovieEntity>{
        return MoviesRemoteMediator(apiService, appDatabase , baseUrl)
    }

    @Singleton
    @Provides
    fun providePagingConfig() = PagingConfig(pageSize = 20)

    @Singleton
    @Provides
    fun providePager(pagingConfig : PagingConfig, moviesRemoteMediator : RemoteMediator<Int, MovieEntity>, movieDao: MovieDao) = Pager(
        config = pagingConfig,
        remoteMediator = moviesRemoteMediator ,
        pagingSourceFactory = { movieDao.pagingSource() }
    )
}