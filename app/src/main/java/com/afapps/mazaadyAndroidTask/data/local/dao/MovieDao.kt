package com.afapps.mazaadyAndroidTask.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.afapps.mazaadyAndroidTask.data.local.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertAll(movies: List<MovieEntity>)


    @Query("SELECT * FROM MovieEntity")
    fun pagingSource():PagingSource<Int , MovieEntity>

    @Query("SELECT page FROM MovieEntity ORDER BY localId DESC LIMIT 1")
    fun getLastPage(): Int

    @Query("SELECT COUNT(*) FROM MovieEntity")
    suspend fun getCountMovies(): Int

    @Query("SELECT * FROM MovieEntity WHERE id =(:id)")
    suspend fun getMovieDetailsById(id: Long) : MovieEntity?

    @Query("UPDATE MovieEntity SET isFavourite = :isFavourite WHERE id = :id")
    suspend fun updateFavouriteStatus(id: Long, isFavourite: Boolean)

    @Query("SELECT id FROM MovieEntity WHERE isFavourite = 1")
    suspend fun getFavouriteMovieIds(): List<Long>

    @Query("SELECT * FROM MovieEntity WHERE isFavourite = 1")
    fun getFavouriteMovies(): Flow<List<MovieEntity>>

    @Query("DELETE  FROM MovieEntity")
    suspend fun clearAll()

}