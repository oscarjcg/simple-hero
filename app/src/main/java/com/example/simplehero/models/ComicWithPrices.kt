package com.example.simplehero.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.models.comic.ComicPrice

data class ComicWithPrices (
    @Embedded
    val comic: Comic,
    @Relation(
        parentColumn = "id",
        entityColumn = "comicId"
    )
    val prices: List<ComicPrice>
)
