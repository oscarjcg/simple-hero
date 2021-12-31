package com.example.simplehero.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ComicPrice(
    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("price")
    @Expose
    val price: Float,
)
