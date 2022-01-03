package com.example.simplehero.models.comic

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

const val PRICE_TYPE_PRINT = "printPrice"
const val PRICE_TYPE_PURCHASE_DIGITAL = "digitalPurchasePrice"

@Entity(tableName = "comic_prices", indices = [Index(value = ["id"], unique = true)])
data class ComicPrice(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("_id")
    @Expose
    val id: Int,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("price")
    @Expose
    val price: Float,

    @SerializedName("_comicId")
    @Expose
    var comicId: Int,
)
