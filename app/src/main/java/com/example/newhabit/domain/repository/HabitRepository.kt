package com.example.newhabit.domain.repository

import com.example.newhabit.domain.model.Habit

interface HabitRepository {

//    suspend fun fetchAll(): List<Habit>

    suspend fun fetch(dayOfWeek: Int): List<Habit>

    suspend fun add(title: String, daysOfWeek: List<Int>)
}