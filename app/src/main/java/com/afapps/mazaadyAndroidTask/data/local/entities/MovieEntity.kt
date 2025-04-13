package com.afapps.mazaadyAndroidTask.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MovieEntity(
	@PrimaryKey(autoGenerate = true)
    var localId: Int = 0,
	var id: Long ?= null,
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
	val page: Int = 0,
	val isFavourite: Boolean = false,
	)
