package com.example.simplehero.models.comiccharacter

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "character", indices = [Index(value = ["id"], unique = true)])
class ComicCharacter (
    @PrimaryKey
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("_createdAt")
    @Expose
    var createdAt: Date? = null,
)
