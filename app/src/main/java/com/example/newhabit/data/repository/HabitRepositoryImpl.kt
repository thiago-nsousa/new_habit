package com.example.newhabit.data.repository

import android.util.Log
import com.example.newhabit.data.local.database.entity.HabitEntity
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.repository.HabitRepository
import java.util.UUID
import kotlin.collections.map
import com.example.newhabit.data.local.database.dao.HabitDao
import com.example.newhabit.domain.model.HabitCategory
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(private val dao: HabitDao) : HabitRepository {

    override suspend fun fetchAll(): List<Habit> {
        Log.d(TAG, "Fetching all habits")
        return dao.fetchAll().map { habit ->
            Habit(
                id = habit.uuid,
                title = habit.title,
                daysOfWeek = habit.daysOfWeek,
                category = habit.category.let { HabitCategory.valueOf(it) }
            )
        }
    }

    override suspend fun fetchByDayOfWeek(dayOfWeek: Int): List<Habit> {
        Log.d(TAG, "Fetching habits for day of week $dayOfWeek")
        return dao.fetchByDayOfWeek(dayOfWeek).map { habit ->
            Habit(
                id = habit.uuid,
                title = habit.title,
                daysOfWeek = habit.daysOfWeek,
                category = habit.category.let { HabitCategory.valueOf(it) }
            )
        }
    }

    override suspend fun fetchById(habitId: String): Habit {
        Log.d(TAG, "Fetching habits for id $habitId")
        val habitEntity =  dao.fetchHabitById(habitId)

        return Habit(
            id = habitEntity.uuid,
            title = habitEntity.title,
            daysOfWeek = habitEntity.daysOfWeek,
            category = habitEntity.category.let { HabitCategory.valueOf(it) }
        )
    }

    override suspend fun add(title: String, daysOfWeek: List<Int>, category: HabitCategory, reminderEnabled: Boolean, reminderHour: Int, reminderMinute: Int): String? {
        Log.d(TAG, "Adding new Habit $title for days $daysOfWeek")
        val habit = HabitEntity(
            uuid = UUID.randomUUID().toString(),
            title = title,
            daysOfWeek = daysOfWeek,
            category = category.name,
        )
        dao.insert(habit)

        return habit.uuid
    }

    override suspend fun delete(habit: Habit) {
        Log.d(TAG, "Delete habit on habitId: ${habit.id}")
        val habitEntity = HabitEntity(
            uuid = habit.id,
            title = habit.title,
            daysOfWeek = habit.daysOfWeek,
            category = habit.category.name
        )
        dao.delete(habitEntity)
    }

    override suspend fun update(habit: Habit) {
        Log.d(TAG, "Delete habit on habitId: ${habit.id}")
        val habitEntity = HabitEntity(
            uuid = habit.id,
            title = habit.title,
            daysOfWeek = habit.daysOfWeek,
            category = habit.category.name
        )
        dao.update(habitEntity)
    }

    companion object {
        private const val TAG = "HabitRepository"
    }
}