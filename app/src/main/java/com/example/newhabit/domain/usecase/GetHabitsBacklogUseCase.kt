package com.example.newhabit.domain.usecase

import android.util.Log
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.model.HabitCategory
import com.example.newhabit.domain.repository.HabitRepository
import javax.inject.Inject

interface GetHabitsBacklogUseCase {
    suspend operator fun invoke(): List<Habit>
}

class GetHabitsBacklogUseCaseImpl @Inject constructor(
    private val habitRepository: HabitRepository,
) : GetHabitsBacklogUseCase {
    override suspend fun invoke(): List<Habit> {

        Log.d(TAG, "Fetching all habits")

        return habitRepository
            .fetchAll()
            .map { habit ->
                Habit(
                    id = habit.id,
                    title = habit.title,
                    daysOfWeek = habit.daysOfWeek,
                    category = habit.category
                )
            }
    }

    companion object {
        private const val TAG = "GetHabitsBacklog"
    }
}