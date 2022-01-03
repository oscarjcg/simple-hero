package com.example.simplehero.models

import com.example.simplehero.models.comic.Comic
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataResponse<T> {
    @SerializedName("offset")
    @Expose
    var offset: Int? = null

    @SerializedName("limit")
    @Expose
    var limit: Int? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("count")
    @Expose
    var count: Int? = null

    @SerializedName("results")
    @Expose
    var results: ArrayList<T>? = null
}
