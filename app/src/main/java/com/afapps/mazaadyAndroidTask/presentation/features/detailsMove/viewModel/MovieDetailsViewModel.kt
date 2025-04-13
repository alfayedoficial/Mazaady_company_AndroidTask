package com.afapps.mazaadyAndroidTask.presentation.features.detailsMove.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afapps.mazaadyAndroidTask.R
import com.afapps.mazaadyAndroidTask.domain.model.Movie
import com.afapps.mazaadyAndroidTask.domain.repo.MovieRepo
import com.afapps.mazaadyAndroidTask.presentation.features.detailsMove.state.MovieState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepo: MovieRepo,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val movieState: StateFlow<MovieState>
        get() = _movieState
    private val _movieState = MutableStateFlow<MovieState>(MovieState.Loading)

    fun getMovieDetails(movieId: Long) {
        viewModelScope.launch {
            val movie = movieRepo.getMovieDetailsById(movieId)
            delay(1000) // Simulate loading delay
            if (movie != null){
                _movieState.emit(MovieState.Success(movie))
            }else{
                _movieState.emit(MovieState.Error(context.getString(R.string.something_went_wrong)))
            }
        }
    }


    fun updateFavouriteStatus(id: Long, movie: Movie) {
        viewModelScope.launch {
            movieRepo.updateFavouriteStatus(id, !movie.isFavourite)
            _movieState.emit(MovieState.Success(movie.copy(isFavourite = !movie.isFavourite)))
        }
    }
}