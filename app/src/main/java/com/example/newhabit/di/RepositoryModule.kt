package com.example.newhabit.di

import com.example.newhabit.data.repository.HabitProgressRepositoryImpl
import com.example.newhabit.data.repository.HabitRepositoryImpl
import com.example.newhabit.domain.repository.HabitProgressRepository
import com.example.newhabit.domain.repository.HabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun providesHabitRepository(impl: HabitRepositoryImpl): HabitRepository

    @Singleton
    @Binds
    abstract fun providesHabitProgressRepository(impl: HabitProgressRepositoryImpl): HabitProgressRepository
}