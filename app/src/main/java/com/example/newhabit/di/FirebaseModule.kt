package com.example.newhabit.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}
