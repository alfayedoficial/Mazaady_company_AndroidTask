package com.afapps.mazaadyAndroidTask.presentation.features.detailsMove.state

import com.afapps.mazaadyAndroidTask.domain.model.Movie

sealed class MovieState{
    data object Loading : MovieState()
    data class Success(val movie: Movie?) : MovieState()
    data class Error(val message: String) : MovieState()
}