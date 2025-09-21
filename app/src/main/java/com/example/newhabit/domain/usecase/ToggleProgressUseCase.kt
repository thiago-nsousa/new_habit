package com.example.newhabit.domain.usecase

import android.icu.util.Calendar
import com.example.newhabit.domain.repository.HabitProgressRepository

interface ToggleProgressUseCase {
    suspend operator fun invoke(habitId: String)
}

class ToggleProgressUseCaseImpl(
    private val progressRepository: HabitProgressRepository
) : ToggleProgressUseCase {

    override suspend fun invoke(habitId: String) {
        val today = Calendar.getInstance()
        val progress = progressRepository.fetch(habitId = habitId, today.timeInMillis)
        if (progress.isNotEmpty()) {
            progressRepository.delete(progress.first().id)
        } else {
            progressRepository.add(habitId = habitId)
        }
    }
}