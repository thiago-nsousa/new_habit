package com.example.newhabit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.newhabit.databinding.ActivityMainBinding
import com.example.newhabit.presentation.form.HabitFormFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupNavigation()
        setupBottomNavigation()
        setupFloatingButton()
    }

    private fun setupFloatingButton() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val floatingButton = binding.root.findViewById<FloatingActionButton>(R.id.fab)
        floatingButton.setOnClickListener {
            navController.navigate(R.id.action_habitList_to_habitForm)
        }
    }
    private fun setupBottomNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        bottomNavigation = binding.root.findViewById(R.id.bottomNavigationView)
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
        val navController = findNavController(R.id.nav_host_fragment_content_main)
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
}