package com.example.simplehero.webservices

import com.example.simplehero.models.comiccharacter.ComicCharacter
import com.example.simplehero.models.WrapperResponse
import com.example.simplehero.models.comic.Comic
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterWebService {

    @GET("characters/{characterId}/comics")
    suspend fun getComics(
        @Path("characterId") characterId: Int,
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,): WrapperResponse<Comic>

    @GET("characters/{characterId}")
    suspend fun getCharacter(
        @Path("characterId") characterId: Int,
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,): WrapperResponse<ComicCharacter>
}
