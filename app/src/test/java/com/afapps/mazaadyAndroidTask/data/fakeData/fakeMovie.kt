package com.afapps.mazaadyAndroidTask.data.fakeData

import com.afapps.mazaadyAndroidTask.data.local.entities.MovieEntity
import com.afapps.mazaadyAndroidTask.data.remote.dto.MovieDto

object DataFake {

    private val _fakeMovieDto = MovieDto(
        id = 323,
        overview = "Test OverView",
        originalLanguage = null,
        originalTitle = null,
        video = null,
        title = "Title",
        popularity = null,
        adult = null,
    )

    private val _fakeDataMoviesDtoList = listOf(
        _fakeMovieDto,
        _fakeMovieDto,
        _fakeMovieDto,
        _fakeMovieDto,
        _fakeMovieDto
    )
    val fakeDataMoviesDtoList = _fakeDataMoviesDtoList


    private val _fakeMovieEntity = MovieEntity(
        localId = 4384,
        id = 323,
        overview = "Test OverView",
        originalLanguage = null,
        originalTitle = null,
        video = null,
        title = "Title",
        posterPath = null,
        backdropPath = null,
        releaseDate = null,
        popularity = null,
        voteAverage = null,
        adult = null,
        voteCount = null,
        page = 3077
    )
    val fakeMovieEntity = _fakeMovieEntity

    val fakeMovieEntityList = listOf(
        _fakeMovieEntity.copy(localId = 1,id = 1),
        _fakeMovieEntity.copy(localId = 2,id = 2),
        _fakeMovieEntity.copy(localId = 3,id = 3),
        _fakeMovieEntity.copy(localId = 4,id = 4)
    )
}