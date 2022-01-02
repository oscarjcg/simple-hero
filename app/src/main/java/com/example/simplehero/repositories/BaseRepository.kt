package com.example.simplehero.repositories

import com.example.simplehero.models.ComicWithPrices
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.utils.CACHE_INTERVAL_DAYS
import com.example.simplehero.utils.DAY_MS

open class BaseRepository {
    fun isCacheValid(cacheTime: Long): Boolean {
        val now = System.currentTimeMillis()
        val cacheDateExpiration = cacheTime + (CACHE_INTERVAL_DAYS * DAY_MS)
        return now < cacheDateExpiration
    }

    fun buildComic(comicWithPrices: ComicWithPrices): Comic {
        comicWithPrices.comic.prices = ArrayList(comicWithPrices.prices)
        return comicWithPrices.comic
    }

    fun buildComics(comicWithPrices: List<ComicWithPrices>): List<Comic> {
        val comics = ArrayList<Comic>()
        comicWithPrices.forEach { comicWithPrice ->
            comicWithPrice.comic.prices = ArrayList(comicWithPrice.prices)
            comics.add(comicWithPrice.comic)
        }

        return comics
    }
}
