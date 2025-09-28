package com.example.newhabit.data.remote.dto

import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.model.HabitCategory

data class HabitDto(
    val id: String,
    val title: String,
    val daysOfWeek: List<Int>,
    val category: String
)

fun HabitDto.toDomain() = Habit(
    id = id,
    title = title,
    daysOfWeek = daysOfWeek,
    category = category.let { HabitCategory.valueOf(it) }
)