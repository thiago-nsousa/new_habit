package com.example.newhabit.data.repository.firebase

import android.util.Log
import com.example.newhabit.data.remote.dto.HabitProgressDTO
import com.example.newhabit.data.remote.dto.toDomain
import com.example.newhabit.domain.model.HabitProgress
import com.example.newhabit.domain.repository.HabitProgressRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class HabitProgressRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : HabitProgressRepository {

    private val userId: String
        get() = auth.currentUser?.uid.orEmpty()

    private fun progressCollection(habitId: String) =
        firestore.collection("users").document(userId)
            .collection("habits").document(habitId)
            .collection("progress")

    override suspend fun fetch(habitId: String, completedAt: Long): List<HabitProgress> {
        val snapshot = progressCollection(habitId)
            .get().await()

        return snapshot.documents.mapNotNull { it.toObject<HabitProgressDTO>()?.toDomain() }
    }

    override suspend fun delete(habitId: String, progressId: String) {
        try {
            progressCollection(habitId).document(progressId).delete().await()
        } catch (e: Exception) {
            Log.d("HabitProgressRepository", e.message.toString())
        }

    }

    override suspend fun add(habitId: String) {
        val today = android.icu.util.Calendar.getInstance()
        val dayOfWeek = today.get(android.icu.util.Calendar.DAY_OF_WEEK)

        val documentRef = progressCollection(habitId).document()
        val progress = HabitProgressDTO(
            id = documentRef.id,
            habitId = habitId,
            completedAt = today.timeInMillis,
            dayOfWeek = dayOfWeek
        )
        documentRef.set(progress).await()
    }
}
