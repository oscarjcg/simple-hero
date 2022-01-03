package com.example.simplehero.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simplehero.database.dao.ComicCharacterDao
import com.example.simplehero.database.dao.ComicDao
import com.example.simplehero.models.comiccharacter.ComicCharacter
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.models.comic.ComicPrice

/**
 * Room database.
 */
@Database(entities = [
    Comic::class,
    ComicPrice::class,
    ComicCharacter::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun comicDao(): ComicDao
    abstract fun comicCharacterDao(): ComicCharacterDao
}
