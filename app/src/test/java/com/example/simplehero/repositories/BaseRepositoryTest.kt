package com.example.simplehero.repositories

import com.example.simplehero.models.comic.ComicWithPrices
import com.example.simplehero.models.DataResponse
import com.example.simplehero.models.WrapperResponse
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.models.comic.ComicPrice
import com.example.simplehero.models.comiccharacter.ComicCharacter
import com.example.simplehero.utils.OpResult
import java.util.*
import kotlin.collections.ArrayList

open class BaseRepositoryTest {
    fun createFakeResponseComics(n: Int, comicId: Int): WrapperResponse<Comic> {
        val response = WrapperResponse<Comic>()
        val comicDataResponse = DataResponse<Comic>()
        comicDataResponse.results = createFakeComics(n, comicId)
        response.data = comicDataResponse
        return response
    }

    fun createFakeComicsWithPrices(n: Int, comicId: Int): ArrayList<ComicWithPrices>  {
        val comics = createFakeComics(n, comicId)
        val comicsPrices = createFakeComicPrices(n ,comicId)
        val comicsWithPrices = ArrayList<ComicWithPrices>()
        for (i in comics.indices) {
            val prices = ArrayList<ComicPrice>()
            prices.add(comicsPrices[i])
            val comicWithPrices = ComicWithPrices(comics[i], prices)

            comicsWithPrices.add(comicWithPrices)
        }
        return comicsWithPrices
    }

    private fun createFakeComics(n: Int, comicId: Int): ArrayList<Comic> {
        val comics = ArrayList<Comic>()
        val createdAt = Date()
        for (i in 1..n) {
            val comic = Comic(comicId + i)
            comic.createdAt = createdAt
            comics.add(comic)
        }
        return comics
    }

    private fun createFakeComicPrices(n: Int, comicId: Int): ArrayList<ComicPrice> {
        val comicsPrices = ArrayList<ComicPrice>()
        for (i in 1..n) {
            val comicPrice = ComicPrice(i, "", 0f, comicId)
            comicsPrices.add(comicPrice)
        }
        return comicsPrices
    }

    fun buildComics(comicWithPrices: List<ComicWithPrices>): List<Comic> {
        val comics = ArrayList<Comic>()
        comicWithPrices.forEach { comicWithPrice ->
            comicWithPrice.comic.prices = ArrayList(comicWithPrice.prices)
            comics.add(comicWithPrice.comic)
        }

        return comics
    }

    fun <T> getOpResultData(opResult: OpResult<T>): T {
        return (opResult as OpResult.Success).data
    }

    fun createFakeResponseCharacter(n: Int, characterId: Int): WrapperResponse<ComicCharacter> {
        val response = WrapperResponse<ComicCharacter>()
        val characterDataResponse = DataResponse<ComicCharacter>()
        characterDataResponse.results = createFakeCharacters(n, characterId)
        response.data = characterDataResponse
        return response
    }

    fun createFakeCharacters(n: Int, characterId: Int): ArrayList<ComicCharacter> {
        val characters = ArrayList<ComicCharacter>()
        val createdAt = Date()
        for (i in 1..n) {
            val character = ComicCharacter(characterId)
            character.createdAt = createdAt
            characters.add(character)
        }
        return characters
    }
}
