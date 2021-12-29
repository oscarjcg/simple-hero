package com.example.simplehero.di

import com.example.simplehero.repositories.CharacterRepository
import com.example.simplehero.repositories.ComicRepository
import com.example.simplehero.webservices.CharacterWebService
import com.example.simplehero.webservices.ComicWebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCharacterRepository(characterWebService: CharacterWebService): CharacterRepository {
        return CharacterRepository(characterWebService)
    }

    @Singleton
    @Provides
    fun provideComicRepository(comicWebService: ComicWebService): ComicRepository {
        return ComicRepository(comicWebService)
    }
}
