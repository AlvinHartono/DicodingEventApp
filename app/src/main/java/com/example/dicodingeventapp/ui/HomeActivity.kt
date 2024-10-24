package com.example.dicodingeventapp.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.databinding.ActivityHomeBinding
import com.example.dicodingeventapp.notification.MyWorker
import com.example.dicodingeventapp.ui.ui.search.SearchActivity
import com.example.dicodingeventapp.ui.ui.settings.SettingsPreferences
import com.example.dicodingeventapp.ui.ui.settings.SettingsViewModel
import com.example.dicodingeventapp.ui.ui.settings.SettingsViewModelFactory
import com.example.dicodingeventapp.ui.ui.settings.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest
    private lateinit var binding: ActivityHomeBinding
    private lateinit var settingsViewModel: SettingsViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setSupportActionBar(binding.upcomingToolbar)


        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_upcoming,
                R.id.navigation_finished,
                R.id.navigation_favorite,
                R.id.navigation_settings,
            )
        )

        val pref = SettingsPreferences.getInstance(application.dataStore)
        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(pref = pref)
        )[SettingsViewModel::class.java]


        // observe dark mode
        settingsViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }

        // Init WorkManager
        workManager = WorkManager.getInstance(this)
        // observe notification
        setupNotificationToggle()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupNotificationToggle() {
        settingsViewModel.getNotificationSetting().observe(this) { isNotificationEnabled ->
            if (isNotificationEnabled) {
                startPeriodicTask()
            } else {
                cancelPeriodicTask()
            }
        }
    }


    private fun startPeriodicTask() {
        val data = Data.Builder()
            .putString("title", "Dicoding Event App")
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        periodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 1, TimeUnit.DAYS)
                .setInputData(data)
                .setConstraints(constraints)
                .build()

        workManager.enqueue(periodicWorkRequest)


        // Observe work status
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@HomeActivity) { workInfo ->
                workInfo?.let {
                    val status = it.state.name
                    Log.d("HomeActivity", "WorkInfo status: $status")

                } ?: run {
                    Log.d("HomeActivity", "WorkInfo is null, no active work found")
                }
            }
    }

    private fun cancelPeriodicTask() {
        if (::periodicWorkRequest.isInitialized) {
            workManager.cancelWorkById(periodicWorkRequest.id)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                // Handle search action
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}