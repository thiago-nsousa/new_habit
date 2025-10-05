package com.example.newhabit.data.remote.dto

import com.example.newhabit.domain.model.HabitProgress
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class HabitProgressDTO(
    val id: String = "",
    val habitId: String = "",
    val dayOfWeek: Int = 0,
    val completedAt: Long? = null
)

fun HabitProgressDTO.toDomain() = HabitProgress(
    id = id,
    habitId = habitId,
    dayOfWeek = dayOfWeek,
)
