package com.example.simplehero.repositories

import com.example.simplehero.database.dao.ComicDao
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.models.WrapperResponse
import com.example.simplehero.models.comic.ComicPrice
import com.example.simplehero.utils.*
import com.example.simplehero.webservices.CharacterWebService
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class CharacterRepository(private val characterWebService: CharacterWebService,
                          private val comicDao: ComicDao) : BaseRepository() {

    suspend fun getComics(characterId: Int, offset: Int, limit: Int): OpResult<List<Comic>> {
        val comicsCache = getCache(characterId, offset, limit)
        if (comicsCache.isNotEmpty())
            return OpResult.Success(comicsCache)

        val wrapperResponse: WrapperResponse
        try {
            val apikeyGen = ApiKeyGenerator()
            wrapperResponse =
                characterWebService.getComics(characterId, apikeyGen.ts, APIKEY, apikeyGen.hash, offset, limit)
        } catch (e: Exception) {
            return OpResult.Error(e)
        }

        val comics = wrapperResponse.data!!.results!!
        saveCache(characterId, comics, offset)
        return OpResult.Success(comics)
    }

    private suspend fun saveCache(characterId: Int, comics: List<Comic>, offset: Int) {
        for (i in comics.indices) {
            val comic = comics[i]
            comic.createdAt = Date()
            comic.characterId = characterId
            comic.order = offset + i

            comic.prices?.let { prices ->
                saveComicPrices(prices, comic.id)
            }
        }
        comicDao.addAllComics(comics)
    }

    private suspend fun saveComicPrices(prices: List<ComicPrice>, comicId: Int) {
        prices.forEach { price ->
            price.comicId = comicId
        }
        comicDao.addAllComicPrices(prices.toList())
    }

    private suspend fun getCache(characterId: Int, offset: Int, limit: Int): List<Comic> {
        val comicWithPrices =  comicDao.getComicsWithPrices(characterId, limit, offset)
        if (comicWithPrices.isNotEmpty()) {
            val cacheDate = comicWithPrices[0].comic.createdAt
            if (cacheDate != null && !isCacheValid(cacheDate.time)) {
                deleteComics()
                return ArrayList()
            }
        }
        return buildComics(comicWithPrices)
    }

    private suspend fun deleteComics() {
        comicDao.deleteAllComicsPrices()
        comicDao.deleteAllComics()
    }
}
