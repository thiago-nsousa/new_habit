package com.example.newhabit.data.local.entity

import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.model.HabitProgress

data class HabitProgressEntity(
    val id: String,
    val habitId: String,
    val dayOfWeek: Int,
)

fun HabitProgressEntity.toDomain() = HabitProgress(
    id = id,
    habitId = habitId,
    dayOfWeek = dayOfWeek,
)
