package com.example.simplehero.di

import com.example.simplehero.repositories.ComicRepository
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
    fun provideComicRepository(): ComicRepository {
        return ComicRepository()
    }
}
