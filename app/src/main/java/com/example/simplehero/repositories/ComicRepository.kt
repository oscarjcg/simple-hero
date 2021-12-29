package com.example.simplehero.repositories

import com.example.simplehero.models.Comic
import com.example.simplehero.webservices.ComicWebService

private const val TS = "1"
private const val APIKEY = "65f0ecd939fe80d8ea679bbf5ffdaabe"
private const val HASH = "6fa0eed9ec9817ede58edaadf9c9ab4a"

class ComicRepository(private val comicWebService: ComicWebService) {

    suspend fun getComic(comicId: Int): Comic {
        // TODO: Review this
        val response = comicWebService.getComic(comicId, TS, APIKEY, HASH)
        return response.data!!.results!![0]
    }
}
