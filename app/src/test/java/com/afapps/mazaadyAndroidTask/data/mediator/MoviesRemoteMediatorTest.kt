package com.afapps.mazaadyAndroidTask.data.mediator

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.afapps.mazaadyAndroidTask.data.fakeData.DataFake
import com.afapps.mazaadyAndroidTask.data.local.dao.MovieDao
import com.afapps.mazaadyAndroidTask.data.local.database.AppDatabase
import com.afapps.mazaadyAndroidTask.data.local.entities.MovieEntity
import com.afapps.mazaadyAndroidTask.data.remote.api.ApiService
import com.afapps.mazaadyAndroidTask.data.remote.dto.DiscoverMoveResponseDTO
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by [Ali Al Fayed](https://www.linkedin.com/in/alfayedoficial)
 * About class :
 * Date Created: 06/07/2024 - 3:31 PM
 * Last Modified: 06/07/2024 - 3:31 PM
 */

@Suppress("DEPRECATION")
@OptIn(ExperimentalPagingApi::class)
@RunWith(AndroidJUnit4::class)
class MoviesRemoteMediatorTest {


    @MockK
    lateinit var apiService: ApiService
    private lateinit var _movieDao: MovieDao
    private lateinit var _sut: MoviesRemoteMediator //system under test
    private val baseUrl = "https://api.themoviedb.org/3/"


    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()

        _movieDao = appDatabase.movieDao()
        MockKAnnotations.init(this)
        _sut = MoviesRemoteMediator(apiService, appDatabase, baseUrl)
    }

    @Test
    fun `refresh Load Returns SuccessResult When More Data Is Present`() = runTest {
        //give
        coEvery {
            apiService.getPopularMoviesApi(any(), any())
        } returns DiscoverMoveResponseDTO(
            page = null,
            totalPages = null,
            results = DataFake.fakeDataMoviesDtoList.toMutableList(),
            totalResults = null
        )

        val pagingState = PagingState<Int, MovieEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = _sut.load(LoadType.REFRESH, pagingState)
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()
    }

    @Test
    fun `refresh Load Returns Error Result When Error Occurs`() = runTest {
        coEvery {
            apiService.getPopularMoviesApi(any(), any())
        } throws IOException()

        val pagingState = PagingState<Int, MovieEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = _sut.load(LoadType.REFRESH, pagingState)
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Error::class.java)

    }

    @After
    fun tearDown() = runTest {
        _movieDao.clearAll()
    }
}