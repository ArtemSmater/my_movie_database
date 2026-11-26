package com.example.mymoviedatabase.data.dto.trailer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class TrailerDto(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("key")
    @Expose
    var key: String? = null
)