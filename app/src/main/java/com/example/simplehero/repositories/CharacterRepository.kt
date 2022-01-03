package com.example.simplehero.repositories

import com.example.simplehero.database.dao.ComicCharacterDao
import com.example.simplehero.database.dao.ComicDao
import com.example.simplehero.models.comiccharacter.ComicCharacter
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.models.WrapperResponse
import com.example.simplehero.models.comic.ComicPrice
import com.example.simplehero.utils.*
import com.example.simplehero.webservices.CharacterWebService
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class CharacterRepository(private val characterWebService: CharacterWebService,
                          private val comicDao: ComicDao,
                          private val characterDao: ComicCharacterDao) : BaseRepository() {

    suspend fun getComics(characterId: Int, offset: Int, limit: Int): OpResult<List<Comic>> {
        // Check cache
        val comicsCache = getCache(characterId, offset, limit)
        if (comicsCache.isNotEmpty())
            return OpResult.Success(comicsCache)

        // Request webservice
        val wrapperResponse: WrapperResponse<Comic>
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

    /**
     * Get comics from cache checking if they are not expired. Delete all if expired.
     */
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

    suspend fun getCharacter(characterId: Int): OpResult<ComicCharacter> {
        // Check cache
        val characterCache = getCharacterCache(characterId)
        if (characterCache != null)
            return OpResult.Success(characterCache)

        // Request webservice
        val wrapperResponse: WrapperResponse<ComicCharacter>
        try {
            val apikeyGen = ApiKeyGenerator()
            wrapperResponse =
                characterWebService.getCharacter(characterId, apikeyGen.ts, APIKEY, apikeyGen.hash)
        } catch (e: Exception) {
            return OpResult.Error(e)
        }

        val character = wrapperResponse.data!!.results!![0]
        saveCache(character)

        return OpResult.Success(character)
    }

    /**
     * Get character from cache checking if it is expired. Delete all if expired.
     */
    private suspend fun getCharacterCache(characterId: Int): ComicCharacter? {
        val comicCharacter =  characterDao.getCharacter(characterId)
        if (comicCharacter != null) {
            val cacheDate = comicCharacter.createdAt
            if (cacheDate != null && isCacheValid(cacheDate.time))
                return comicCharacter
        }
        return null
    }

    private suspend fun saveCache(character: ComicCharacter) {
        character.createdAt = Date()
        characterDao.addCharacter(character)
    }
}
