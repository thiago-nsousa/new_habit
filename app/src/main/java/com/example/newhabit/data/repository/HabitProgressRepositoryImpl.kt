package com.example.newhabit.data.repository

import android.icu.util.Calendar
import android.util.Log
import com.example.newhabit.data.local.AppDatabase
import com.example.newhabit.data.local.entity.HabitProgressEntity
import com.example.newhabit.data.remote.dto.toDomain
import com.example.newhabit.domain.model.HabitProgress
import com.example.newhabit.domain.repository.HabitProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class HabitProgressRepositoryImpl(appDatabase: AppDatabase) : HabitProgressRepository {

    private val dao = appDatabase.progressDao()

    override suspend fun fetch(habitId: String, completedAt: Long): List<HabitProgress> {
        return dao.fetchProgressByHabit(habitId, completedAt).map { progress ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = progress.completedAt
            HabitProgress(
                id = progress.uuid,
                habitId = habitId,
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK),
            )
        }
    }

    override suspend fun delete(id: String) {
        Log.d(LOG_TAG, "Losing progress on habitId: $id")
        dao.delete(id)
    }

    override suspend fun add(habitId: String) {
        val today = Calendar.getInstance()
        val dayOfWeek = today.get(Calendar.DAY_OF_WEEK)

        Log.d(LOG_TAG, "Making progress on $dayOfWeek for habitId: $habitId")

        val progress = HabitProgressEntity(
            uuid = UUID.randomUUID().toString(),
            habitId = habitId,
            completedAt = today.timeInMillis,
        )
        withContext(Dispatchers.IO) {
            dao.insert(progress)
        }
    }

    companion object {
        private const val LOG_TAG = "HabitProgressRepository"
    }
}