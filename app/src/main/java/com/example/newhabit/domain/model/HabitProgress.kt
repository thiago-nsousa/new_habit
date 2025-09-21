package com.example.newhabit.domain.model

data class HabitProgress(
    val id: String,
    val habitId: String,
    val dayOfWeek: Int,
)
