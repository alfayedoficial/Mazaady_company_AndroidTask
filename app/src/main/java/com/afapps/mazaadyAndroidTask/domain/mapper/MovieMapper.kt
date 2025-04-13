package com.afapps.mazaadyAndroidTask.domain.mapper

import com.afapps.mazaadyAndroidTask.data.local.entities.MovieEntity
import com.afapps.mazaadyAndroidTask.domain.model.Movie


fun MovieEntity.toMovie(): Movie {
    return Movie(
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
        isFavourite = isFavourite
    )
}

fun MovieEntity?.toMovieOrNull(): Movie? {
    return if (this == null){
        return null
    }else{
        Movie(
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
            isFavourite = isFavourite
        )
    }
}