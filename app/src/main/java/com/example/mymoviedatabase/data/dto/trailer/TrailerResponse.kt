package com.example.mymoviedatabase.data.dto.trailer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class TrailerResponse(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("results")
    @Expose
    var results: MutableList<TrailerDto>? = null
)