package com.example.newhabit.domain.model

data class Habit(
    val id: String,
    val title: String,
    val daysOfWeek: List<Int>,
    val isCompleted: Boolean = false,
)
