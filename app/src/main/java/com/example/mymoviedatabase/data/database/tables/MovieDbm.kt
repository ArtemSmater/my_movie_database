package com.example.mymoviedatabase.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_movies")
data class MovieDbm(
    @PrimaryKey(autoGenerate = true)
    val uniqueId: Int = 0,

    val category: String,

    val pageNumber: Int,

    val id: Long? = null,

    val backdropPath: String? = null,

    val originalLanguage: String? = null,

    val originalTitle: String? = null,

    val overview: String? = null,

    val popularity: Double? = null,

    val posterPath: String? = null,

    val releaseDate: String? = null,

    val title: String? = null,

    val voteAverage: Double? = null,

    val voteCount: Int? = null,
)