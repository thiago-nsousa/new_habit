package com.example.newhabit.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newhabit.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object AuthSuccess : AuthState()
    object PasswordResetEmailSent : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("Email e senha não podem estar vazios.")
                return@launch
            }
            _authState.value = AuthState.Loading

            val result = authRepository.signInWithEmailAndPassword(email, password)

            result.onSuccess {
                _authState.value = AuthState.AuthSuccess
            }.onFailure {  error ->
                _authState.value = AuthState.Error("Falha no login: ${error.message}")
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signInWithGoogle(idToken)

            result.onSuccess {
                _authState.value = AuthState.AuthSuccess
            }.onFailure { error ->
                _authState.value = AuthState.Error("Falha no login com Google: ${error.message}")
            }
        }
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("E-mail e senha são obrigatórios.")
                return@launch
            }
            if (password.length < 6) {
                _authState.value = AuthState.Error("A senha deve ter no mínimo 6 caracteres.")
                return@launch
            }
            _authState.value = AuthState.Loading
            val result = authRepository.createUserWithEmailAndPassword(email, password)

            result.onSuccess {
                _authState.value = AuthState.AuthSuccess
            }.onFailure { error ->
                _authState.value = AuthState.Error("Falha no registro: ${error.message}")
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            if (email.isBlank()) {
                _authState.value = AuthState.Error("O e-mail não pode estar vazio.")
                return@launch
            }
            _authState.value = AuthState.Loading
            val result = authRepository.sendPasswordResetEmail(email)

            result.onSuccess {
                _authState.value = AuthState.PasswordResetEmailSent
            }.onFailure { error ->
                _authState.value = AuthState.Error("Falha ao enviar e-mail de redefinição: ${error.message}")
            }
        }
    }
}
