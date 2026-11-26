package com.example.mymoviedatabase.data.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
data class FavouriteMovieDbm(
    @PrimaryKey(autoGenerate = true)
    val uniqueId: Int = 0,

    var backdropPath: String? = null,

    var id: Long? = null,

    var originalLanguage: String? = null,

    var originalTitle: String? = null,

    var overview: String? = null,

    var popularity: Double? = null,

    var smallPosterPath: String? = null,

    var bigPosterPath: String? = null,

    var releaseDate: String? = null,

    var title: String? = null,

    var voteAverage: Double? = null,

    var voteCount: Int? = null,
)