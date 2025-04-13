package com.afapps.mazaadyAndroidTask.viewModel

import android.content.Context
import com.afapps.mazaadyAndroidTask.R
import com.afapps.mazaadyAndroidTask.domain.model.Movie
import com.afapps.mazaadyAndroidTask.domain.repo.MovieRepo
import com.afapps.mazaadyAndroidTask.presentation.features.detailsMove.state.MovieState
import com.afapps.mazaadyAndroidTask.presentation.features.detailsMove.viewModel.MovieDetailsViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var sut: MovieDetailsViewModel

    private val movieRepo: MovieRepo = mockk()
    private val context: Context = mockk()

    private val fakeMovie = Movie(id = 1, title = "Test", overview = "Test", isFavourite = false)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sut = MovieDetailsViewModel(movieRepo, context)
    }

    @Test
    fun `getMovieDetails emits Success when movie exists`() = runTest {
        // Given
        coEvery { movieRepo.getMovieDetailsById(1L) } returns fakeMovie

        // When
        sut.getMovieDetails(1L)
        advanceTimeBy(1000)
        advanceUntilIdle()

        // Then
        val result = sut.movieState.first()
        assertThat(result).isInstanceOf(MovieState.Success::class.java)
        assertThat((result as MovieState.Success).movie!!.id).isEqualTo(fakeMovie.id)
    }

    @Test
    fun `getMovieDetails emits Error when movie is null`() = runTest {
        // Given
        coEvery { movieRepo.getMovieDetailsById(1L) } returns null
        every { context.getString(R.string.something_went_wrong) } returns "Something went wrong"

        // When
        sut.getMovieDetails(1L)
        advanceTimeBy(1000)
        advanceUntilIdle()

        // Then
        val result = sut.movieState.first()
        assertThat(result).isInstanceOf(MovieState.Error::class.java)
        assertThat((result as MovieState.Error).message).isEqualTo("Something went wrong")
    }

    @Test
    fun `updateFavouriteStatus toggles isFavourite and emits Success`() = runTest {
        // Given
        coEvery { movieRepo.updateFavouriteStatus(1L, true) } just Runs

        // When
        sut.updateFavouriteStatus(1L, fakeMovie)
        advanceUntilIdle()

        // Then
        val result = sut.movieState.first()
        assertThat(result).isInstanceOf(MovieState.Success::class.java)
        assertThat((result as MovieState.Success).movie!!.isFavourite).isTrue()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
