package com.example.newhabit.data.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // Obtenha os dados passados para o Worker (título do hábito, etc.)
        val habitTitle = inputData.getString(KEY_HABIT_TITLE) ?: "Lembrete de Hábito"
        NotificationHelper.showHabitReminderNotification(context, habitTitle)

        return Result.success()
    }

    companion object {
        const val KEY_HABIT_TITLE = "habit_title"
    }
}
