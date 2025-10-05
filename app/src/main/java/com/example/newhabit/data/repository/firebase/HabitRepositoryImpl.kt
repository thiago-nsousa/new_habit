package com.example.newhabit.data.repository.firebase

import com.example.newhabit.data.remote.dto.HabitDTO
import com.example.newhabit.data.remote.dto.toDTO
import com.example.newhabit.data.remote.dto.toDomain
import com.example.newhabit.domain.model.Habit
import com.example.newhabit.domain.model.HabitCategory
import com.example.newhabit.domain.repository.HabitRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : HabitRepository {

    private val userId: String
        get() = auth.currentUser?.uid.orEmpty()

    private val habitsCollection
        get() = firestore.collection("users").document(userId).collection("habits")

    override suspend fun fetchAll(): List<Habit> {
        val snapshot = habitsCollection.get().await()
        return snapshot.documents.mapNotNull { it.toObject<HabitDTO>()?.toDomain() }
    }

    override suspend fun fetchByDayOfWeek(dayOfWeek: Int): List<Habit> {
        val snapshot = habitsCollection.whereArrayContains("daysOfWeek", dayOfWeek).get().await()
        return snapshot.documents.mapNotNull { it.toObject<HabitDTO>()?.toDomain() }
    }

    override suspend fun fetchById(habitId: String): Habit {
        val document = habitsCollection.document(habitId).get().await()
        return document.toObject<HabitDTO>()?.toDomain()
            ?: throw NoSuchElementException("Habit with id $habitId not found")
    }

    override suspend fun add(title: String, daysOfWeek: List<Int>, category: HabitCategory, reminderEnabled: Boolean, reminderHour: Int, reminderMinute: Int): String? {
        val documentRef = habitsCollection.document()
        val newHabit = Habit(
            id = documentRef.id,
            title = title,
            daysOfWeek = daysOfWeek,
            isCompleted = false,
            category = category,
            reminderEnabled = reminderEnabled,
            reminderHour = reminderHour,
            reminderMinute = reminderMinute
        )
        documentRef.set(newHabit.toDTO(userId)).await()

        return documentRef.id
    }

    override suspend fun delete(habit: Habit) {
        habitsCollection.document(habit.id).delete().await()
    }

    override suspend fun update(habit: Habit) {
        habitsCollection.document(habit.id).set(habit.toDTO(userId)).await()
    }
}

