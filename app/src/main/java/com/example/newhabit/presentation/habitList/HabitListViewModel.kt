package com.example.newhabit.presentation.habitList

import androidx.lifecycle.ViewModel
import com.example.newhabit.domain.usecase.GetHabitsForTodayUseCase
import com.example.newhabit.domain.usecase.ToggleProgressUseCase
import androidx.lifecycle.*
import com.example.newhabit.domain.model.Habit
import kotlinx.coroutines.launch

data class HabitListState(val habitItemList: List<Habit>)

class HabitListViewModel(
    private val getHabitsForTodayUseCase: GetHabitsForTodayUseCase,
    private val toggleProgressUseCase: ToggleProgressUseCase,
) : ViewModel() {

     // Mutable Live Data that initialize with the current list of saved Habits.
    private val _uiState: MutableLiveData<HabitListState> by lazy {
        MutableLiveData(HabitListState(habitItemList = emptyList()))
    }
    val uiState: LiveData<HabitListState> = _uiState

    init {
        viewModelScope.launch {
            refreshHabitList()
        }
    }

    // Refresh UI State whenever View Resumes.
    fun refresh() {
        viewModelScope.launch {
            refreshHabitList()
        }
    }

    fun toggleHabitCompleted(habitId: String) {
        viewModelScope.launch {
            toggleProgressUseCase(habitId)
            refreshHabitList()
        }
    }

    private suspend fun refreshHabitList() {
        _uiState.postValue(HabitListState(getHabitsForTodayUseCase()))
    }


    // ViewModel Factory needed to provide Repository injection to ViewModel.
    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val toggleProgressUseCase: ToggleProgressUseCase,
        private val getHabitsForTodayUseCase: GetHabitsForTodayUseCase,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HabitListViewModel(getHabitsForTodayUseCase, toggleProgressUseCase) as T
        }
    }
}