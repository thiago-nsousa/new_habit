package com.example.newhabit.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newhabit.MainActivity
import com.example.newhabit.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Verifica o usuário atual ANTES de inflar o layout
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Fecha a AuthActivity
            return // Impede que o resto do onCreate seja executado
        }

        setContentView(R.layout.activity_auth)
    }
}
