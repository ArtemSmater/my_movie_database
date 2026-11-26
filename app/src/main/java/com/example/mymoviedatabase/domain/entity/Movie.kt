package com.example.mymoviedatabase.domain.entity

import java.io.Serializable


data class Movie(
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
) : Serializable