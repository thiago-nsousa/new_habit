package com.example.newhabit.data.repository

import android.util.Log
import com.example.newhabit.data.local.entity.HabitEntity
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.repository.HabitRepository
import java.util.UUID
import kotlin.collections.map
import com.example.newhabit.data.local.AppDatabase

class HabitRepositoryImpl(appDatabase: AppDatabase) : HabitRepository {

    private val dao = appDatabase.habitDao()

    override suspend fun fetch(dayOfWeek: Int): List<Habit> {
        Log.d(TAG, "Fetching habits for day of week $dayOfWeek")
        return dao.fetchByDayOfWeek(dayOfWeek).map {
            Habit(
                id = it.uuid,
                title = it.title,
                daysOfWeek = it.daysOfWeek
            )
        }
    }

    override suspend fun add(title: String, daysOfWeek: List<Int>) {
        Log.d(TAG, "Adding new Habit $title for days $daysOfWeek")
        val habit = HabitEntity(
            uuid = UUID.randomUUID().toString(),
            title = title,
            daysOfWeek = daysOfWeek
        )
        dao.insert(habit)
    }

    companion object {
        private const val TAG = "HabitRepository"
    }
}