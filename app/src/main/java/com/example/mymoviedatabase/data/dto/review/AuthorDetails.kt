package com.example.mymoviedatabase.data.dto.review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AuthorDetails(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("username")
    @Expose
    var username: String? = null,

    @SerializedName("avatar_path")
    @Expose
    var avatarPath: String? = null,

    @SerializedName("rating")
    @Expose
    var rating: Int? = null
)