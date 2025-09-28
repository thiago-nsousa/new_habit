package com.example.newhabit.presentation.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newhabit.domain.repository.HabitRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.model.HabitCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HabitState(val habit: Habit)

@HiltViewModel
class HabitFormViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _uiState: MutableLiveData<HabitState> by lazy {
        MutableLiveData(HabitState(Habit("", "", emptyList(), false, HabitCategory.OTHER)))
    }
    val uiState: LiveData<HabitState> = _uiState
    fun addHabit(name: String, habitDaysSelected: List<Int>, category: HabitCategory) {
        viewModelScope.launch {
            habitRepository.add(name, habitDaysSelected, category)
        }
    }

    fun deleteHabit() {
        viewModelScope.launch {
            habitRepository.delete(_uiState.value!!.habit)
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
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