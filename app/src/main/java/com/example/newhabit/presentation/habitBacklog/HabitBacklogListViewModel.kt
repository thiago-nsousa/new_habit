package com.example.newhabit.presentation.habitBacklog

import androidx.lifecycle.ViewModel
import com.example.newhabit.domain.usecase.GetHabitsForTodayUseCase
import com.example.newhabit.domain.usecase.ToggleProgressUseCase
import androidx.lifecycle.*
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.usecase.GetHabitsBacklogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HabitBacklogListState(val habitItemList: List<Habit>)

@HiltViewModel
class HabitBacklogListViewModel @Inject constructor(
    private val getHabitsBacklogUseCase: GetHabitsBacklogUseCase,
) : ViewModel() {

     // Mutable Live Data that initialize with the current list of saved Habits.
    private val _uiState: MutableLiveData<HabitBacklogListState> by lazy {
        MutableLiveData(HabitBacklogListState(habitItemList = emptyList()))
    }
    val uiState: LiveData<HabitBacklogListState> = _uiState

    init {
        viewModelScope.launch {
            fetchAllHabitList()
        }
    }

    // Refresh UI State whenever View Resumes.
    fun refresh() {
        viewModelScope.launch {
            fetchAllHabitList()
        }
    }

    private suspend fun fetchAllHabitList() {
        _uiState.postValue(HabitBacklogListState(getHabitsBacklogUseCase()))
    }
}