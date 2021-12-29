package com.example.simplehero.webservices

import com.example.simplehero.models.ComicResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ComicWebService {
    @GET("comics/{comicId}")
    suspend fun getComic(
        @Path("comicId") comicId: Int,
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String): ComicResponse
}
