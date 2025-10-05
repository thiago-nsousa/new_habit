package com.example.newhabit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.newhabit.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var floatingButton: FloatingActionButton
    private lateinit var navController: NavController

    // Declarar o launcher para o pedido de permissão
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                // Opcional: Mostrar uma mensagem a explicar por que a permissão é importante
                // se o utilizador a negou.
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "As notificações são importantes para os lembretes. Pode ativá-las nas configurações.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        floatingButton = binding.root.findViewById(R.id.fab)
        bottomNavigation = binding.root.findViewById(R.id.bottomNavigationView)

        setSupportActionBar(binding.toolbar)
        setupNavigation()
        setupBottomNavigation()
        setupFloatingButton()
        setupDestinationListener()
        askNotificationPermission()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("FCM", "Falha ao obter o token FCM ${task.exception}")
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "Token FCM do Dispositivo: ${token}", )
        }

    }

    private fun setupFloatingButton() {
        floatingButton.setOnClickListener {
            navController.navigate(R.id.action_habitList_to_habitForm)
        }
    }
    private fun setupBottomNavigation() {
        NavigationUI.setupWithNavController(bottomNavigation, navController)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.habitListFragment, R.id.habitBacklogListFragment) // fragments de topo
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val toolbar = binding.toolbar
            // Mostrar ou esconder ícone de navegação
            if (appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                toolbar.navigationIcon = null // remove o ícone
            }
        }
    }

    private fun setupNavigation() {
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
        return true
    }

    // Handle action bar item clicks here. The action bar will
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.action_habitList_to_settingsFragment)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun setupDestinationListener() {
        // Adiciona um listener para mudanças de destino na navegação
        navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.habitListFragment,
                    R.id.habitBacklogListFragment -> {
                        bottomNavigation.visibility = View.VISIBLE
                        floatingButton.visibility = View.VISIBLE
                    }
                    else -> {
                        bottomNavigation.visibility = View.GONE
                        floatingButton.visibility = View.GONE
                    }
                }
        }
    }

    private fun askNotificationPermission() {
        // 3. A verificação só é necessária para o Android 13 (API 33) ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 4. Verificar se a permissão JÁ FOI CONCEDIDA
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // 5. Se não foi concedida, lançar o pedido de permissão
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}