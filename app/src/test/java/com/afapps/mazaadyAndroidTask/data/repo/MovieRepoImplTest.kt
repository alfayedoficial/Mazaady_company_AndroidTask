@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("DEPRECATION")

package com.afapps.mazaadyAndroidTask.data.repo

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import app.cash.turbine.test
import com.afapps.mazaadyAndroidTask.data.fakeData.DataFake.fakeMovieEntity
import com.afapps.mazaadyAndroidTask.data.fakeData.DataFake.fakeMovieEntityList
import com.afapps.mazaadyAndroidTask.data.helper.collectDataForTest
import com.afapps.mazaadyAndroidTask.data.local.dao.MovieDao
import com.afapps.mazaadyAndroidTask.data.local.database.AppDatabase
import com.afapps.mazaadyAndroidTask.data.local.entities.MovieEntity
import com.afapps.mazaadyAndroidTask.domain.mapper.toMovie
import com.afapps.mazaadyAndroidTask.domain.model.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by [Ali Al Fayed](https://www.linkedin.com/in/alfayedoficial)
 * About class :
 * Date Created: 06/07/2024 - 3:44 PM
 * Last Modified: 06/07/2024 - 3:44 PM
 */


@RunWith(AndroidJUnit4::class)
class MovieRepoImplTest {

    @MockK
    lateinit var pager: Pager<Int, MovieEntity>
    lateinit var movieDao: MovieDao
    lateinit var sut: MovieRepoImpl //system under test
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
        movieDao = appDatabase.movieDao()
        MockKAnnotations.init(this)
        sut = MovieRepoImpl(movieDao, pager)
    }

    @Test
    fun `given existing id, when call getMovieDetailsById then return Movie `() = runTest {
        // given
        movieDao.upsertAll(listOf(fakeMovieEntity))

        // when
        val actualMovie = sut.getMovieDetailsById(323)

        //then
        assertThat(actualMovie).isNotNull()
    }

    @Test
    fun `given not existing id, when call getMovieDetailsById then return null `() = runTest {
        //given
        val id = 300L

        // when
        val actualMovie = sut.getMovieDetailsById(id)

        //then
        assertThat(actualMovie).isNull()
    }

    @Test
    fun `given list when call  getDiscoverMoviesPagingFlow then return flow paging data`() = runTest {
            //given
            val list = listOf(
                fakeMovieEntity,
                fakeMovieEntity.copy(id = 2),
                fakeMovieEntity.copy(id = 3)
            )

            //when
            coEvery {
                pager.flow
            } returns flow {
                emit(PagingData.from(list))
            }

            sut.getDiscoverMoviesPagingFlow().test {
                val pagingData = awaitItem()

                val result = pagingData.collectDataForTest(object : DiffUtil.ItemCallback<Movie>() {
                    override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                        oldItem.id == newItem.id

                    override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                        oldItem == newItem
                })

                assertThat(result).isNotEmpty()
                assertThat(result.first().id).isEqualTo(fakeMovieEntity.id)

                cancelAndIgnoreRemainingEvents()
            }

        }

    @Test
    fun `getFavouriteMovies returns mapped movie list`() = runTest {
        // Given
        val fakeFavouriteMovies = listOf(
            fakeMovieEntity.copy(localId = 1, id = 1, isFavourite = true),
            fakeMovieEntity.copy(localId = 2, id = 2, isFavourite = true),
            fakeMovieEntity.copy(localId = 3, id = 3, isFavourite = true),
            fakeMovieEntity.copy(localId = 4, id = 4, isFavourite = true),
            fakeMovieEntity.copy(localId = 5, id = 5, isFavourite = true)
        )
        movieDao.upsertAll(fakeFavouriteMovies)

        // When
        val result = sut.getFavouriteMovies().first()

        // Then
        val expectedMovieList = fakeFavouriteMovies.map { it.toMovie() }
        assertThat(result).containsExactlyElementsIn(expectedMovieList)
    }

    @Test
    fun `updateFavouriteStatus calls dao with correct values`() = runTest {
        // Given
        movieDao.upsertAll(fakeMovieEntityList)

        // When
        sut.updateFavouriteStatus(1L, true)

        // Then
        val updatedMovie = movieDao.getMovieDetailsById(1L)
        assertThat(updatedMovie?.isFavourite).isTrue()
    }

    @After
    fun tearDown() = runTest {
        movieDao.clearAll()
        Dispatchers.resetMain()
    }
}