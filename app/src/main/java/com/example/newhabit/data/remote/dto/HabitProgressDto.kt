package com.example.newhabit.data.remote.dto

import com.example.newhabit.domain.model.HabitProgress

data class HabitProgressDto(
    val id: String,
    val habitId: String,
    val dayOfWeek: Int,
)

fun HabitProgressDto.toDomain() = HabitProgress(
    id = id,
    habitId = habitId,
    dayOfWeek = dayOfWeek,
)
