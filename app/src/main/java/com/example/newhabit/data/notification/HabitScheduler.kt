package com.example.newhabit.data.notification

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object HabitScheduler {

    /**
     * Agenda um lembrete diário e periódico para um hábito específico.
     *
     * @param context Contexto da aplicação.
     * @param habitId O ID único do hábito, usado para identificar o Worker.
     * @param habitTitle O título do hábito a ser exibido na notificação.
     * @param hour A hora do dia para o lembrete (0-23).
     * @param minute O minuto da hora para o lembrete (0-59).
     */
    fun scheduleDailyReminder(
        context: Context,
        habitId: String,
        habitTitle: String,
        hour: Int,
        minute: Int
    ) {
        val workManager = WorkManager.getInstance(context)

        // 1. Calcular o atraso inicial até a próxima hora do lembrete
        val now = Calendar.getInstance()
        val scheduledTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        if (scheduledTime.before(now)) {
            // Se a hora já passou hoje, agendar para amanhã
            scheduledTime.add(Calendar.DAY_OF_YEAR, 1)
        }
        val initialDelay = scheduledTime.timeInMillis - now.timeInMillis

        // 2. Passar o título do hábito para o Worker
        val inputData = Data.Builder()
            .putString(ReminderWorker.KEY_HABIT_TITLE, habitTitle)
            .build()

        // 3. Criar um pedido de trabalho periódico (executa a cada 24h)
        val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        // 4. Agendar o trabalho com um nome único para evitar duplicação
        //    REPLACE irá substituir um agendamento antigo pelo novo se o utilizador editar o hábito
        workManager.enqueueUniquePeriodicWork(
            habitId, // Usar o ID do hábito como nome único do trabalho
            ExistingPeriodicWorkPolicy.REPLACE,
            reminderRequest
        )
    }

    /**
     * Cancela um lembrete agendado para um hábito.
     *
     * @param context Contexto da aplicação.
     * @param habitId O ID do hábito cujo lembrete deve ser cancelado.
     */
    fun cancelReminder(context: Context, habitId: String) {
        WorkManager.getInstance(context).cancelUniqueWork(habitId)
    }
}
