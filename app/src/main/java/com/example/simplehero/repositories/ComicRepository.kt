package com.example.simplehero.repositories

import com.example.simplehero.database.dao.ComicDao
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.models.WrapperResponse
import com.example.simplehero.models.comic.ComicPrice
import com.example.simplehero.utils.*
import com.example.simplehero.webservices.ComicWebService
import java.lang.Exception
import java.util.*

class ComicRepository(private val comicWebService: ComicWebService,
                      private val comicDao: ComicDao) : BaseRepository() {

    suspend fun getComic(comicId: Int): OpResult<Comic> {
        // Check cache
        val comicCache = getCache(comicId)
        if (comicCache != null)
            return OpResult.Success(comicCache)

        // Request webservice
        val wrapperResponse: WrapperResponse<Comic>
        try {
            val apikeyGen = ApiKeyGenerator()
            wrapperResponse = comicWebService.getComic(comicId, apikeyGen.ts, APIKEY, apikeyGen.hash)
        } catch (e: Exception) {
            return OpResult.Error(e)
        }

        val comic = wrapperResponse.data!!.results!![0]
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

    /**
     * Get comic from cache checking if it is expired. Delete all if expired.
     */
    private suspend fun getCache(comicId: Int): Comic? {
        val comicWithPrices =  comicDao.getComicWithPrice(comicId)
        if (comicWithPrices != null) {
            val cacheDate = comicWithPrices.comic.createdAt
            if (cacheDate != null && isCacheValid(cacheDate.time))
                return buildComic(comicWithPrices)
        }
        return null
    }

}
