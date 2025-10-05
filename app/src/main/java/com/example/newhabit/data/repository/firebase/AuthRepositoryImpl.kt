package com.example.newhabit.data.repository.firebase

import android.webkit.ConsoleMessage
import com.example.newhabit.domain.model.User
import com.example.newhabit.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    private fun FirebaseUser.toDomainUser(): User {
        return User(
            uid = this.uid,
            email = this.email
        )
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!.toDomainUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            Result.success(result.user!!.toDomainUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user!!.toDomainUser())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toDomainUser()
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    /**
     * Implementação da nova função.
     * Obtém o token FCM atual e guarda-o no Firestore para o utilizador logado.
     */
    override suspend fun saveCurrentUserFcmToken(): Result<Unit> {
        return try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Result.failure(Exception("Utilizador não autenticado para salvar o token."))

            // 1. Obter o token FCM atual do dispositivo de forma assíncrona
            val token = FirebaseMessaging.getInstance().token.await()

            // 2. Definir o caminho no Firestore: /users/{userId}/tokens/{token}
            val tokenRef = FirebaseFirestore.getInstance()
                .collection("users").document(userId)
                .collection("tokens").document(token)

            // 3. Salvar o token (usar o próprio token como ID previne duplicados)
            val tokenInfo = mapOf("createdAt" to FieldValue.serverTimestamp())
            tokenRef.set(tokenInfo).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}