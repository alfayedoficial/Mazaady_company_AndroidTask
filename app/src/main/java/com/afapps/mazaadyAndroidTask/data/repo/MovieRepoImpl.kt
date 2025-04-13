package com.afapps.mazaadyAndroidTask.data.repo

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.afapps.mazaadyAndroidTask.data.local.dao.MovieDao
import com.afapps.mazaadyAndroidTask.data.local.entities.MovieEntity
import com.afapps.mazaadyAndroidTask.domain.mapper.toMovie
import com.afapps.mazaadyAndroidTask.domain.mapper.toMovieOrNull
import com.afapps.mazaadyAndroidTask.domain.model.Movie
import com.afapps.mazaadyAndroidTask.domain.repo.MovieRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepoImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val pager: Pager<Int, MovieEntity>
) : MovieRepo {

    override fun getDiscoverMoviesPagingFlow(): Flow<PagingData<Movie>> {
        return pager.flow.map { pagingData -> pagingData.map { it.toMovie() } }
    }


    override suspend fun getMovieDetailsById(id: Long): Movie? {
        return movieDao.getMovieDetailsById(id).toMovieOrNull()
    }


    override fun getFavouriteMovies(): Flow<List<Movie>> {
        return movieDao.getFavouriteMovies().map { movies -> movies.map { it.toMovie() } }
    }

    override suspend fun updateFavouriteStatus(id: Long, isFavourite: Boolean) {
       movieDao.updateFavouriteStatus(id, isFavourite)
    }


}