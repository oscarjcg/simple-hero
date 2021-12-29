package com.example.simplehero.repositories

import com.example.simplehero.models.Comic
import com.example.simplehero.webservices.CharacterWebService

private const val TS = "1"
private const val APIKEY = "65f0ecd939fe80d8ea679bbf5ffdaabe"
private const val HASH = "6fa0eed9ec9817ede58edaadf9c9ab4a"

class CharacterRepository(private val characterWebService: CharacterWebService) {

    suspend fun getComics(characterId: Int, offset: Int, limit: Int): List<Comic> {
        // TODO: Review this
        val comics = characterWebService.getComics(characterId, TS, APIKEY, HASH, offset, limit)
        return comics.data!!.results!!
    }

}
