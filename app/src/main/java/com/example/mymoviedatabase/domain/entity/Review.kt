package com.example.mymoviedatabase.domain.entity

data class Review(
    var username: String? = null,

    var avatarPath: String? = null,

    var rating: Int? = null,

    var content: String? = null,

    var createdAt: String? = null
)