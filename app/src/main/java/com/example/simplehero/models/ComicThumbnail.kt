package com.example.simplehero.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ComicThumbnail(
    @SerializedName("path")
    @Expose
    val path: String,

    @SerializedName("extension")
    @Expose
    val extension: String
)
