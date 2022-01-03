package com.example.simplehero.database.dao

import androidx.room.*
import com.example.simplehero.models.comiccharacter.ComicCharacter

@Dao
interface ComicCharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacter(character: ComicCharacter)

    @Query("SELECT * FROM character WHERE id=:characterId")
    suspend fun getCharacter(characterId: Int): ComicCharacter

    @Query("DELETE FROM character")
    suspend fun deleteAllCharacters()
}
