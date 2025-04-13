package com.afapps.mazaadyAndroidTask.presentation.features.favourites.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afapps.mazaadyAndroidTask.domain.repo.MovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val movieRepo: MovieRepo,
) : ViewModel() {

    val favouritesMovies = movieRepo.getFavouriteMovies()

    fun removeMovieFromFavourites(movieId: Long) {
       viewModelScope.launch {
           movieRepo.updateFavouriteStatus(movieId , false)
       }
    }
}