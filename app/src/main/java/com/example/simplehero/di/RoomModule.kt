package com.example.simplehero.di

import android.content.Context
import androidx.room.Room
import com.example.simplehero.database.AppDatabase
import com.example.simplehero.database.dao.ComicDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "simple_hero_room_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideComicDao(database: AppDatabase): ComicDao {
        return database.comicDao()
    }
}
