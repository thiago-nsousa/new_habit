package com.example.newhabit.data.repository

import android.util.Log
import com.example.newhabit.data.local.entity.HabitEntity
import com.example.newhabit.data.local.entity.toDomain
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.repository.HabitRepository
import java.util.UUID
import kotlin.collections.map

object HabitRepositoryImpl : HabitRepository {

    private val habitListCache: MutableList<HabitEntity> = mutableListOf(
        HabitEntity(
            id = UUID.randomUUID().toString(),
            title = "Read the book",
            daysOfWeek = listOf(1, 4, 6)
        ),
        HabitEntity(
            id = UUID.randomUUID().toString(),
            title = "Walk the dog",
            daysOfWeek = listOf(1, 4, 6)
        ),
        HabitEntity(
            id = UUID.randomUUID().toString(),
            title = "Drink water",
            daysOfWeek = listOf(1, 4, 6)
        ),
    )

    override suspend fun fetchAll() = habitListCache.map{ it.toDomain() }

    override suspend fun fetch(dayOfWeek: Int): List<Habit> {
        Log.d(TAG, "Fetch Habit by day of week $dayOfWeek")
        return habitListCache.map{ it.toDomain() }.filter {
            it.daysOfWeek.contains(dayOfWeek)
        }
    }

    override suspend fun add(title: String, daysOfWeek: List<Int>) {
        val habit = HabitEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            daysOfWeek = daysOfWeek
        )

        Log.d(TAG, "Adding new Habit $title for days $daysOfWeek")

        habitListCache.add(habit)
    }

    private const val TAG = "HabitRepository"
}