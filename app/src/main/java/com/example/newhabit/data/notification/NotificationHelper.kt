package com.example.newhabit.data.notification

import android.util.Log

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.newhabit.MainActivity
import com.example.newhabit.R

object NotificationHelper {

    // Canais distintos para diferentes tipos de notificação (melhor prática)
    private const val HABIT_REMINDER_CHANNEL_ID = "HABIT_REMINDER_CHANNEL"
    private const val FCM_CHANNEL_ID = "FCM_CHANNEL"

    private const val TAG = "NotificationHelper"

    /**
     * Mostra uma notificação de lembrete de hábito (agendada localmente).
     */
    fun showHabitReminderNotification(context: Context, habitTitle: String) {
        Log.d(TAG, "A tentar mostrar notificação de LEMBRETE para: ${habitTitle}")
        createNotificationChannel(context, HABIT_REMINDER_CHANNEL_ID, "Lembretes de Hábitos")

        val builder = NotificationCompat.Builder(context, HABIT_REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon) // Certifique-se que este ícone existe
            .setContentTitle("Não se esqueça do seu hábito!")
            .setContentText(habitTitle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createPendingIntent(context)) // Adicionado para abrir a app
            .setAutoCancel(true)

        showNotification(context, System.currentTimeMillis().toInt(), builder)
    }

    /**
     * Mostra uma notificação vinda do Firebase (remota).
     */
    fun showFirebaseNotification(context: Context, title: String, body: String) {
        Log.d(TAG, "A tentar mostrar notificação do FIREBASE com título: ${title}")
        createNotificationChannel(context, FCM_CHANNEL_ID, "Notificações Gerais")

        val builder = NotificationCompat.Builder(context, FCM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon) // Certifique-se que este ícone existe
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Usar prioridade alta para FCM
            .setContentIntent(createPendingIntent(context)) // Adicionado para abrir a app
            .setAutoCancel(true)

        showNotification(context, System.currentTimeMillis().toInt(), builder)
    }

    /**
     * Função centralizada para verificar permissões e exibir a notificação.
     */
    private fun showNotification(context: Context, notificationId: Int, builder: NotificationCompat.Builder) {
        // PONTO CRÍTICO: Verificar a permissão antes de tentar notificar (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "PERMISSÃO DE NOTIFICAÇÃO NÃO CONCEDIDA. A notificação não será exibida.")
                return // Abortar se a permissão não foi concedida
            }
        }

        // Usar NotificationManagerCompat para maior compatibilidade
        with(NotificationManagerCompat.from(context)) {
            Log.d(TAG,"A enviar notificação para o sistema com ID: ${notificationId}")
            notify(notificationId, builder.build())
        }
    }

    /**
     * Cria um PendingIntent para abrir a MainActivity.
     */
    private fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    /**
     * Cria o canal de notificação (necessário para Android 8.0+).
     * É seguro chamar esta função várias vezes; o sistema ignora se o canal já existir.
     */
    private fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Canal para notificações da aplicação de hábitos"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            Log.d(TAG, "A registar o canal de notificação com ID: ${channelId}")
            notificationManager.createNotificationChannel(channel)
        }
    }
}

