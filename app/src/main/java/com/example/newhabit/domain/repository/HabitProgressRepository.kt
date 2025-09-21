package com.example.newhabit.domain.repository

import com.example.newhabit.domain.model.HabitProgress

interface HabitProgressRepository {

    /**
     *
     * @param habitId ID of the specific Habit
     * @param completedAt time in millis when this habit was completed
     */
    suspend fun fetch(habitId: String, completedAt: Long): List<HabitProgress>

    /**
     * @param id ID of the progress
     */
    suspend fun delete(id: String)

    /**
     * @param habitId ID of the specific Habit that we make progress
     */
    suspend fun add(habitId: String)
}