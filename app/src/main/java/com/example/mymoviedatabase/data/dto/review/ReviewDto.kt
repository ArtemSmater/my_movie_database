package com.example.mymoviedatabase.data.dto.review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ReviewDto(
    @SerializedName("author_details")
    @Expose
    var authorDetails: AuthorDetails? = null,

    @SerializedName("content")
    @Expose
    var content: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("id")
    @Expose
    var id: String? = null
)