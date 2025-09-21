package com.example.newhabit.data.repository

import android.icu.util.Calendar
import android.util.Log
import com.example.newhabit.data.local.entity.HabitProgressEntity
import com.example.newhabit.data.local.entity.toDomain
import com.example.newhabit.domain.model.HabitProgress
import com.example.newhabit.domain.repository.HabitProgressRepository
import java.util.UUID

object HabitProgressRepositoryImpl : HabitProgressRepository {

    private val progressListCache: MutableList<HabitProgressEntity> = mutableListOf()

    override suspend fun fetch(habitId: String, completedAt: Long): List<HabitProgress> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = completedAt
        return progressListCache.map {it.toDomain()}.filter {
            it.habitId == habitId && it.dayOfWeek == calendar.get(Calendar.DAY_OF_WEEK)
        }
    }

    override suspend fun delete(id: String) {
        Log.d(LOG_TAG, "Losing progress on habitId: $id")
        progressListCache.removeAll { it.id == id }
    }

    override suspend fun add(habitId: String) {
        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val progress = HabitProgressEntity(
            id = UUID.randomUUID().toString(),
            habitId = habitId,
            dayOfWeek = dayOfWeek
        )

        Log.d(LOG_TAG, "Making progress on $dayOfWeek for habitId: $habitId")

        progressListCache.add(progress)
    }

    private const val LOG_TAG = "HabitProgressRepository"
}