package com.afapps.mazaadyAndroidTask.data.mapper

import com.afapps.mazaadyAndroidTask.data.local.entities.MovieEntity
import com.afapps.mazaadyAndroidTask.data.remote.dto.MovieDto


fun MovieDto.toMovieEntity(page: Int): MovieEntity {
    return MovieEntity(
        id = id,
        overview = overview,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        video = video,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        popularity = popularity,
        voteAverage = voteAverage,
        adult = adult,
        voteCount = voteCount,
        page = page
    )
}
