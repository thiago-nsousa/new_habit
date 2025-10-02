package com.example.newhabit.presentation.auth

import com.example.newhabit.databinding.FragmentLoginBinding
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.newhabit.MainActivity
import com.example.newhabit.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Pega a instância compartilhada do ViewModel que pertence à AuthActivity
    private val viewModel: AuthViewModel by activityViewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            viewModel.signInWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "Falha no login com Google: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGoogleSignIn()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Use a string resource
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.signInWithEmailAndPassword(email, password)
        }

        binding.googleSignInButton.setOnClickListener {
            googleAuthLauncher.launch(googleSignInClient.signInIntent)
        }

        binding.registerTextView.setOnClickListener {
            // Navega para o RegisterFragment usando a action definida no nav_graph
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.forgotPasswordTextView.setOnClickListener {
            showPasswordResetDialog()
        }
    }

    private fun showPasswordResetDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Redefinir Senha")
        val input = EditText(requireContext()).apply { hint = "Digite seu e-mail" }
        builder.setView(input)
        builder.setPositiveButton("Enviar") { dialog, _ ->
            viewModel.sendPasswordResetEmail(input.text.toString())
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collect { state ->
                binding.progressBar.isVisible = state is AuthState.Loading

                when (state) {
                    is AuthState.AuthSuccess -> {
                        Toast.makeText(requireContext(), "Autenticação bem-sucedida!", Toast.LENGTH_SHORT).show()
                        navigateToMainScreen()
                    }
                    is AuthState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                    is AuthState.PasswordResetEmailSent -> {
                        Toast.makeText(requireContext(), "E-mail de redefinição enviado!", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit // Idle, Loading
                }
            }
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita memory leaks
    }
}
