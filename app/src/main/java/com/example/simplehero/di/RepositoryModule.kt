package com.example.simplehero.di

import com.example.simplehero.database.dao.ComicCharacterDao
import com.example.simplehero.database.dao.ComicDao
import com.example.simplehero.repositories.CharacterRepository
import com.example.simplehero.repositories.ComicRepository
import com.example.simplehero.webservices.CharacterWebService
import com.example.simplehero.webservices.ComicWebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides repositories.
 */
@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCharacterRepository(characterWebService: CharacterWebService,
                                   comicDao: ComicDao,
                                   comicCharacterDao: ComicCharacterDao
    ): CharacterRepository {
        return CharacterRepository(characterWebService, comicDao, comicCharacterDao)
    }

    @Singleton
    @Provides
    fun provideComicRepository(comicWebService: ComicWebService,
                               comicDao: ComicDao): ComicRepository {
        return ComicRepository(comicWebService, comicDao)
    }
}
