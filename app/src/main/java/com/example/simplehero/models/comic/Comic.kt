package com.example.simplehero.models.comic

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "comic", indices = [Index(value = ["id"], unique = true)])
data class Comic (
    @PrimaryKey
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("title")
    @Expose
    val title: String? = null,

    @SerializedName("description")
    @Expose
    val description: String? = null,

    @Embedded
    @SerializedName("thumbnail")
    @Expose
    val thumbnail: ComicThumbnail? = null,

    @SerializedName("issueNumber")
    @Expose
    val issueNumber: Float? = null,

    @SerializedName("format")
    @Expose
    val format: String? = null,

    @SerializedName("pageCount")
    @Expose
    val pageCount: Int? = null,

    @SerializedName("_createdAt")
    @Expose
    var createdAt: Date? = null,

    @SerializedName("_charactedId")
    @Expose
    var characterId: Int? = null,

    @SerializedName("_order")
    var order: Int? = null,
) {
    @Ignore
    @SerializedName("prices")
    @Expose
    var prices: ArrayList<ComicPrice>? = null
}
