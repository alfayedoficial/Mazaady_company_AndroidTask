package com.afapps.mazaadyAndroidTask.presentation.features.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import com.afapps.mazaadyAndroidTask.domain.repo.MovieRepo
import com.afapps.mazaadyAndroidTask.utilities.NetworkConnectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepo: MovieRepo,
    val networkConnectionManager: NetworkConnectionManager,
) : ViewModel() {

    val recyclerViewOrientation: StateFlow<Int>
        get() = _recyclerViewOrientation
    private val _recyclerViewOrientation = MutableStateFlow(RecyclerView.VERTICAL)


    val discoverMovies = movieRepo.getDiscoverMoviesPagingFlow()
        .cachedIn(viewModelScope)


    fun updateRecyclerViewOrientation(orientation: Int) {
        _recyclerViewOrientation.value = orientation
    }

    fun updateFavouriteStatus(id: Long, favourite: Boolean) {
        viewModelScope.launch {
            movieRepo.updateFavouriteStatus(id, favourite)
        }
    }
}