package com.example.newhabit.data.notification

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class HabitMessagingService : FirebaseMessagingService() {

    private val TAG = "HabitMessagingService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("onMessageReceived", "FCM remoteMessage: $remoteMessage")

        remoteMessage.notification?.let {
            NotificationHelper.showFirebaseNotification(
                applicationContext,
                it.title ?: "Novo Lembrete",
                it.body ?: "Você tem uma nova mensagem!"
            )
        }

//        val dataPayload = remoteMessage.data
//        var title = dataPayload["title"]
//        var body = dataPayload["body"]
//
//        // Se não houver dados personalizados, tentar obter da notificação (payload 'notification').
//        if (title.isNullOrEmpty() || body.isNullOrEmpty()) {
//            remoteMessage.notification?.let {
//                title = it.title
//                body = it.body
//            }
//        }

//        // Se, após as duas tentativas, tivermos um título e um corpo, mostrar a notificação.
//        if (!title.isNullOrEmpty() && !body.isNullOrEmpty()) {
//            Log.d("onMessageReceived", "A construir notificação com Título: %s, Corpo: ${title} ${body}")
//            NotificationHelper.showFirebaseNotification(applicationContext, title, body)
//        } else {
//            Log.d("onMessageReceived", "Não foi possível extrair título e corpo da mensagem FCM.")
//        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("HabitMessagingService", "FCM Token: $token")
        sendTokenToFirestore(token)
    }

    private fun sendTokenToFirestore(token: String?) {
        if (token == null) return

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.d(TAG, "Utilizador não autenticado, não é possível guardar o token")
            return
        }

        // Guardar o token no caminho: /users/{userId}/tokens/{token}
        // Usar o próprio token como ID do documento para evitar duplicação.
        val tokenInfo = mapOf("createdAt" to FieldValue.serverTimestamp())
        FirebaseFirestore.getInstance()
            .collection("users").document(userId)
            .collection("tokens").document(token)
            .set(tokenInfo)
            .addOnSuccessListener { Log.d(TAG, "Token FCM guardado com sucesso no Firestore") }
            .addOnFailureListener { e -> Log.d(TAG, "Erro ao guardar o token FCM: ${e}") }
    }

}
