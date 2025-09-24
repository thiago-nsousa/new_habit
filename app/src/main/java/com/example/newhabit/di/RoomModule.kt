package com.example.newhabit.di

import android.app.Application
import com.example.newhabit.data.local.database.AppDatabase
import com.example.newhabit.data.local.database.dao.HabitDao
import com.example.newhabit.data.local.database.dao.HabitProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class) // This module will have a singleton lifecycle
@Module
object RoomModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(application: Application): AppDatabase {
        return AppDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun providesHabitDao(database: AppDatabase): HabitDao {
        return database.habitDao()
    }

    @Singleton
    @Provides
    fun providesProgressDao(database: AppDatabase): HabitProgressDao {
        return database.progressDao()
    }
}