package com.example.newhabit.data.remote.dto

import com.example.newhabit.domain.model.Habit

data class HabitDto(
    val id: String,
    val title: String,
    val daysOfWeek: List<Int>,
)

fun HabitDto.toDomain() = Habit(
    id = id,
    title = title,
    daysOfWeek = daysOfWeek,
)