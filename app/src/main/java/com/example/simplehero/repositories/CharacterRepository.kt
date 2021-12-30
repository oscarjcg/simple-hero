package com.example.simplehero.repositories

import com.example.simplehero.models.Comic
import com.example.simplehero.models.ComicResponse
import com.example.simplehero.utils.OpResult
import com.example.simplehero.utils.PRIVATE_APIKEY
import com.example.simplehero.utils.APIKEY
import com.example.simplehero.utils.UtilsFun
import com.example.simplehero.webservices.CharacterWebService
import java.lang.Exception

class CharacterRepository(private val characterWebService: CharacterWebService) {

    suspend fun getComics(characterId: Int, offset: Int, limit: Int): OpResult<List<Comic>> {
        val comicResponse: ComicResponse

        try {
            val ts = System.currentTimeMillis().toString()
            val hash = UtilsFun.md5(ts + PRIVATE_APIKEY + APIKEY)
            comicResponse = characterWebService.getComics(characterId, ts, APIKEY, hash, offset, limit)
        } catch (e: Exception) {
            return OpResult.Error(e)
        }

        return OpResult.Success(comicResponse.data!!.results!!)
    }

}
