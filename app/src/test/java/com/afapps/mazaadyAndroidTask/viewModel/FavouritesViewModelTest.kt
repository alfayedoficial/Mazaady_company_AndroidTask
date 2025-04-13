package com.afapps.mazaadyAndroidTask.viewModel

import app.cash.turbine.test
import com.afapps.mazaadyAndroidTask.domain.model.Movie
import com.afapps.mazaadyAndroidTask.domain.repo.MovieRepo
import com.afapps.mazaadyAndroidTask.presentation.features.favourites.viewModel.FavouritesViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavouritesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var sut: FavouritesViewModel
    private val movieRepo: MovieRepo = mockk()

    private val fakeMovies = listOf(
        Movie(id = 1, title = "Movie 1", overview = "Test OverView", isFavourite = true),
        Movie(id = 2, title = "Movie 2", overview = "Test OverView", isFavourite = true)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // mock the favouritesMovies flow
        every { movieRepo.getFavouriteMovies() } returns flowOf(fakeMovies)

        sut = FavouritesViewModel(movieRepo)
    }


    @Test
    fun `favouritesMovies emits correct data`() = runTest {
        sut.favouritesMovies.test {
            val result = awaitItem()
            assertThat(result).isEqualTo(fakeMovies)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `removeMovieFromFavourites calls updateFavouriteStatus with false`() = runTest {
        // Given
        coEvery { movieRepo.updateFavouriteStatus(1L, false) } just Runs

        // When
        sut.removeMovieFromFavourites(1L)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { movieRepo.updateFavouriteStatus(1L, false) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}
