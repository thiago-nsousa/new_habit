package com.example.newhabit.data.local.entity

import com.example.newhabit.domain.model.Habit

data class HabitEntity(
    val id: String,
    val title: String,
    val daysOfWeek: List<Int>,
)

fun HabitEntity.toDomain() = Habit(
    id = id,
    title = title,
    daysOfWeek = daysOfWeek,
)