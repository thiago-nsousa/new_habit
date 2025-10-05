package com.example.newhabit.di

import android.content.Context
import com.example.newhabit.data.notification.HabitScheduler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Este módulo ensina o Hilt a criar instâncias de classes que não podemos
// anotar com @Inject (como classes de bibliotecas externas).
@Module
@InstallIn(SingletonComponent::class) // A instância viverá enquanto o app viver.
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providesFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun providesHabitScheduler(@ApplicationContext context: Context): HabitScheduler {
        return HabitScheduler
    }
}
