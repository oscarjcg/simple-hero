package com.example.simplehero.repositories

import com.example.simplehero.models.Comic
import com.example.simplehero.models.ComicResponse
import com.example.simplehero.utils.APIKEY
import com.example.simplehero.utils.OpResult
import com.example.simplehero.utils.PRIVATE_APIKEY
import com.example.simplehero.utils.UtilsFun
import com.example.simplehero.webservices.ComicWebService
import java.lang.Exception

class ComicRepository(private val comicWebService: ComicWebService) {

    suspend fun getComic(comicId: Int): OpResult<Comic> {
        val comicResponse: ComicResponse

        try {
            val ts = System.currentTimeMillis().toString()
            val hash = UtilsFun.md5(ts + PRIVATE_APIKEY + APIKEY)
            comicResponse = comicWebService.getComic(comicId, ts, APIKEY, hash)
        } catch (e: Exception) {
            return OpResult.Error(e)
        }

        return OpResult.Success(comicResponse.data!!.results!![0])
    }
}
