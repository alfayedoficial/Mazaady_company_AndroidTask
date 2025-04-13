package com.afapps.mazaadyAndroidTask.domain.repo

import androidx.paging.PagingData
import com.afapps.mazaadyAndroidTask.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepo {

    fun getDiscoverMoviesPagingFlow(): Flow<PagingData<Movie>>

    suspend fun getMovieDetailsById(id:Long): Movie?

    fun getFavouriteMovies(): Flow<List<Movie>>

    suspend fun updateFavouriteStatus(id: Long, isFavourite: Boolean)


}