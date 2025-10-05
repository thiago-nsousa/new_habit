package com.example.newhabit.domain.repository

import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.model.HabitCategory

interface HabitRepository {
    suspend fun fetchAll(): List<Habit>

    suspend fun fetchByDayOfWeek(dayOfWeek: Int): List<Habit>

    suspend fun fetchById(habitId: String): Habit

    suspend fun add(title: String, daysOfWeek: List<Int>, category: HabitCategory)

    suspend fun delete(habit: Habit)

    suspend fun update(habit: Habit)
}