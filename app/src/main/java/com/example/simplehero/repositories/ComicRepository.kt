package com.example.simplehero.repositories

import com.example.simplehero.database.dao.ComicDao
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.models.ComicResponse
import com.example.simplehero.models.ComicWithPrices
import com.example.simplehero.models.comic.ComicPrice
import com.example.simplehero.utils.*
import com.example.simplehero.webservices.ComicWebService
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ComicRepository(private val comicWebService: ComicWebService,
                      private val comicDao: ComicDao) {

    suspend fun getComic(comicId: Int): OpResult<Comic> {
        val comicCache = getCache(comicId)
        if (comicCache != null)
            return OpResult.Success(comicCache)

        val comicResponse: ComicResponse
        try {
            val apikeyGen = ApiKeyGenerator()
            apikeyGen.generate()
            comicResponse = comicWebService.getComic(comicId, apikeyGen.ts, APIKEY, apikeyGen.hash)
        } catch (e: Exception) {
            return OpResult.Error(e)
        }

        val comic = comicResponse.data!!.results!![0]
        saveCache(comic)

        return OpResult.Success(comic)
    }

    private suspend fun saveCache(comic: Comic) {
        comic.createdAt = Date()

        comic.prices?.let { prices ->
            saveComicPrices(prices, comic.id)
        }

        comicDao.addComic(comic)
    }

    private suspend fun saveComicPrices(prices: List<ComicPrice>, comicId: Int) {
        prices.forEach { price ->
            price.comicId = comicId
        }
        comicDao.addAllComicPrices(prices.toList())
    }

    private suspend fun getCache(comicId: Int): Comic? {
        val comicWithPrices =  comicDao.getComicWithPrice(comicId)
        if (comicWithPrices != null)
            return buildComic(comicWithPrices)
        return null
    }

    private fun buildComic(comicWithPrices: ComicWithPrices): Comic {
        comicWithPrices.comic.prices = ArrayList(comicWithPrices.prices)
        return comicWithPrices.comic
    }
}
