package com.example.simplehero.database.dao

import androidx.room.*
import com.example.simplehero.models.ComicWithPrices
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.models.comic.ComicPrice

@Dao
interface ComicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllComics(comics: List<Comic>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addComic(comic: Comic)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllComicPrices(comicPrices: List<ComicPrice>)

    @Transaction
    @Query("SELECT * FROM comic WHERE characterId=:characterId ORDER BY `order` LIMIT :limit OFFSET :offset")
    suspend fun getComicWithPrices(characterId: Int, limit: Int, offset: Int): List<ComicWithPrices>

    @Transaction
    @Query("SELECT * FROM comic WHERE id=:comicId")
    suspend fun getComicWithPrice(comicId: Int): ComicWithPrices
}
