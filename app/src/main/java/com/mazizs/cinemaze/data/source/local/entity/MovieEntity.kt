package com.mazizs.cinemaze.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//Merupakan data yang akan dimasukkan ke dalam database local
@Entity(tableName = "movie_entity")
data class MovieEntity(
    @PrimaryKey
    var id: Int,
    val overview: String? = null,
    var backdropPath: String? = null,
    var posterPath: String? = null,
    var originalLanguage: String,
    var releaseDate: String? = null,
    var voteAverage: Double,
    var title: String,
    var isFavorite: Boolean = false
)