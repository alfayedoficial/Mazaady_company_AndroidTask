package com.afapps.mazaadyAndroidTask.viewModel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import app.cash.turbine.test
import com.afapps.mazaadyAndroidTask.domain.repo.MovieRepo
import com.afapps.mazaadyAndroidTask.presentation.features.home.viewModel.HomeViewModel
import com.afapps.mazaadyAndroidTask.utilities.NetworkConnectionManager
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
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var sut: HomeViewModel // System Under Test

    private val movieRepo: MovieRepo = mockk(relaxed = true)
    private val networkConnectionManager: NetworkConnectionManager = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Mock discoverMovies flow to emit empty paging data
        every { movieRepo.getDiscoverMoviesPagingFlow() } returns flowOf(PagingData.empty())

        sut = HomeViewModel(movieRepo, networkConnectionManager)
    }

    @Test
    fun `updateRecyclerViewOrientation updates orientation state`() = runTest {
        // When
        sut.updateRecyclerViewOrientation(RecyclerView.HORIZONTAL)

        // Then
        assertThat(sut.recyclerViewOrientation.value).isEqualTo(RecyclerView.HORIZONTAL)
    }

    @Test
    fun `updateFavouriteStatus triggers repo update`() = runTest {
        // Given
        coEvery { movieRepo.updateFavouriteStatus(1L, true) } just Runs

        // When
        sut.updateFavouriteStatus(1L, true)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { movieRepo.updateFavouriteStatus(1L, true) }
    }

    @Test
    fun `discoverMovies emits paging data`() = runTest {
        sut.discoverMovies.test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(PagingData::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
