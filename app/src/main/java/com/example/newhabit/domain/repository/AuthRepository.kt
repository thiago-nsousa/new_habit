package com.example.newhabit.domain.repository

import com.example.newhabit.domain.model.User

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<User>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    fun getCurrentUser(): User?
    suspend fun logout()
}

