package com.afapps.mazaadyAndroidTask.domain.model

import com.afapps.mazaadyAndroidTask.BuildConfig

data class Movie(
    val id: Long?,
    val overview: String? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val video: Boolean? = null,
    val title: String? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val popularity: Double? = null,
    val voteAverage: Double? = null,
    val adult: Boolean? = null,
    val voteCount: Int? = null,
    val isFavourite: Boolean = false

){
    fun getImagePath(): String =
        if (posterPath != null) {
            "${BuildConfig.POSTER_URL}/w500$posterPath"
        } else {
            "https://i.stack.imgur.com/GNhx0.png"
        }
}
