package com.example.newhabit.domain.usecase

import android.icu.util.Calendar
import android.util.Log
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.repository.HabitProgressRepository
import com.example.newhabit.domain.repository.HabitRepository
import javax.inject.Inject

interface GetHabitsForTodayUseCase {
    suspend operator fun invoke(): List<Habit>
}

class GetHabitsForTodayUseCaseImpl @Inject constructor(
    private val progressRepository: HabitProgressRepository,
    private val habitRepository: HabitRepository,
) : GetHabitsForTodayUseCase {

    override suspend fun invoke(): List<Habit> {
        val today = Calendar.getInstance()
        val dayOfWeek = today.get(Calendar.DAY_OF_WEEK)

        Log.d(TAG, "Fetching all habits for $dayOfWeek")

        return habitRepository
            .fetch(dayOfWeek)
            .map { habit ->
                Log.d(TAG, "Check we have already work on ${habit.id} at ${today.timeInMillis}")

                val progress = progressRepository.fetch(habit.id, today.timeInMillis)
                val isCompletedToday = progress.isNotEmpty()

                Log.d(TAG, "Habit for today: ${habit.title} is completed: $isCompletedToday")

                Habit(
                    id = habit.id,
                    title = habit.title,
                    daysOfWeek = habit.daysOfWeek,
                    isCompleted = isCompletedToday,
                    category = habit.category
                )
            }
    }

    companion object {
        private const val TAG = "GetHabitsForToday"
    }
}