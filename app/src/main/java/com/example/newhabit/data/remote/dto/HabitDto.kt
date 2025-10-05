package com.example.newhabit.data.remote.dto

import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.model.HabitCategory
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class HabitDTO(
    val id: String = "",
    val title: String = "",
    val daysOfWeek: List<Int> = emptyList(),
    val category: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    val userId: String? = null
)

fun HabitDTO.toDomain() = Habit(
    id = id,
    title = title,
    daysOfWeek = daysOfWeek,
    category = category.let { HabitCategory.valueOf(it) }
)

fun Habit.toDTO(userId: String): HabitDTO {
    return HabitDTO(
        id = this.id,
        title = this.title,
        daysOfWeek = this.daysOfWeek,
        category = this.category.name,
        userId = userId
    )
}