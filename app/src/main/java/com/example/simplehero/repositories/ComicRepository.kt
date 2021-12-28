package com.example.simplehero.repositories

import com.example.simplehero.models.Comic

class ComicRepository() {
    suspend fun getComics(characterId: Int): List<Comic> {
        val dummyComics = ArrayList<Comic>()
        dummyComics.add(Comic("Comic 1"))
        dummyComics.add(Comic("Comic 2"))
        dummyComics.add(Comic("Comic 3"))
        return dummyComics
    }
}
