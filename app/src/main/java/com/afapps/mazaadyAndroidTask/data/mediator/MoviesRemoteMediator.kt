package com.afapps.mazaadyAndroidTask.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.afapps.mazaadyAndroidTask.data.local.database.AppDatabase
import com.afapps.mazaadyAndroidTask.data.local.entities.MovieEntity
import com.afapps.mazaadyAndroidTask.data.mapper.toMovieEntity
import com.afapps.mazaadyAndroidTask.data.remote.NetworkLinks
import com.afapps.mazaadyAndroidTask.data.remote.api.ApiService
import com.afapps.mazaadyAndroidTask.data.remote.getApiLink
import com.afapps.mazaadyAndroidTask.utilities.AppConstants.DEFAULT_FIRST_PAGE
import com.afapps.mazaadyAndroidTask.utilities.annotation.BaseURL
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
    @BaseURL private val baseURl: String,
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieEntity>): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> DEFAULT_FIRST_PAGE
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastPage = appDatabase.movieDao().getLastPage()
                if (lastPage == 0) DEFAULT_FIRST_PAGE else lastPage + 1
            }
        }

        try {
            val response = apiService.getPopularMoviesApi(
                baseURl.getApiLink(NetworkLinks.GetDiscoverMovies.type),
                page = loadKey
            )

            val movies = response.results.orEmpty()

            appDatabase.withTransaction {
                val favouriteMap = if (loadType == LoadType.REFRESH) {
                    // Save a map of ID -> isFavourite
                    appDatabase.movieDao()
                        .getFavouriteMovieIds()
                        .associateWith { true }
                } else emptyMap()

                if (loadType == LoadType.REFRESH) {
                    appDatabase.movieDao().clearAll()
                }

                val movieEntities = movies.map { movieDto ->
                    val entity = movieDto.toMovieEntity(loadKey)
                    if (favouriteMap.containsKey(entity.id)) {
                        entity.copy(isFavourite = true)
                    } else {
                        entity
                    }
                }

                appDatabase.movieDao().upsertAll(movieEntities)
            }

            return MediatorResult.Success(endOfPaginationReached = movies.isEmpty())

        } catch (e: IOException) {
            val localDataExists = appDatabase.movieDao().getCountMovies() > 0
            return if (loadType == LoadType.REFRESH && localDataExists) {
                MediatorResult.Success(endOfPaginationReached = false)
            } else {
                MediatorResult.Error(e)
            }
        } catch (e: HttpException) {
            val localDataExists = appDatabase.movieDao().getCountMovies() > 0
            return if (loadType == LoadType.REFRESH && localDataExists) {
                MediatorResult.Success(endOfPaginationReached = false)
            } else {
                MediatorResult.Error(e)
            }
        }
    }
}