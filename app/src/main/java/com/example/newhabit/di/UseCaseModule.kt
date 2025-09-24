package com.example.newhabit.di


import com.example.newhabit.domain.usecase.GetHabitsForTodayUseCase
import com.example.newhabit.domain.usecase.GetHabitsForTodayUseCaseImpl
import com.example.newhabit.domain.usecase.ToggleProgressUseCase
import com.example.newhabit.domain.usecase.ToggleProgressUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class UseCaseModule {

    @Singleton
    @Binds
    abstract fun providesGetHabitsForTodayUseCase(
        impl: GetHabitsForTodayUseCaseImpl
    ): GetHabitsForTodayUseCase

    @Singleton
    @Binds
    abstract fun providesToggleProgressUseCase(
        impl: ToggleProgressUseCaseImpl
    ): ToggleProgressUseCase
}