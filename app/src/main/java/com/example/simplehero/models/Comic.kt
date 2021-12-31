package com.example.simplehero.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Comic (
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("description")
    @Expose
    val description: String,

    @SerializedName("thumbnail")
    @Expose
    val thumbnail: ComicThumbnail,

    @SerializedName("issueNumber")
    @Expose
    val issueNumber: Float,

    @SerializedName("format")
    @Expose
    val format: String,

    @SerializedName("pageCount")
    @Expose
    val pageCount: Int,

    @SerializedName("prices")
    @Expose
    val prices: ArrayList<ComicPrice>,
)
