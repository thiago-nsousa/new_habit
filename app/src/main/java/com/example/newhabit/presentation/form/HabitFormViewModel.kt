package com.example.newhabit.presentation.form

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newhabit.domain.repository.HabitRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newhabit.data.notification.HabitScheduler
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.model.HabitCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HabitState(val habit: Habit)

@HiltViewModel
class HabitFormViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val habitScheduler: HabitScheduler
) : ViewModel() {

    private val _uiState: MutableLiveData<HabitState> by lazy {
        MutableLiveData(HabitState(Habit("", "", emptyList(), false, HabitCategory.OTHER)))
    }
    val uiState: LiveData<HabitState> = _uiState
    fun addHabit(
        context: Context,
        name: String,
        habitDaysSelected: List<Int>,
        category: HabitCategory,
        isReminderEnabled: Boolean,
        reminderHour: Int,
        reminderMinute: Int
    ) {
        viewModelScope.launch {
            val newHabitId = habitRepository.add(
                name,
                habitDaysSelected,
                category,
                isReminderEnabled,
                reminderHour,
                reminderMinute,
            )

            if (newHabitId != null && isReminderEnabled) {
                habitScheduler.scheduleDailyReminder(
                    context,
                    habitId = "teste",
                    habitTitle = name,
                    hour = reminderHour,
                    minute = reminderMinute
                )
            }
        }
    }

    fun deleteHabit() {
        viewModelScope.launch {
            habitRepository.delete(_uiState.value!!.habit)
        }
    }

    fun updateHabit(
        context: Context,
        habit: Habit
    ) {
        viewModelScope.launch {
            habitScheduler.cancelReminder(context, habit.id)

            habitScheduler.scheduleDailyReminder(
                context = context,
                habitId = habit.id,
                habitTitle = habit.title,
                hour = habit.reminderHour,
                minute = habit.reminderMinute
            )

            habitRepository.update(habit)


        }
    }

    fun fetchHabitById(habitId: String) {
        viewModelScope.launch {

            viewModelScope.launch {
                _uiState.postValue(HabitState(habitRepository.fetchById(habitId)))

            }
        }
    }
}