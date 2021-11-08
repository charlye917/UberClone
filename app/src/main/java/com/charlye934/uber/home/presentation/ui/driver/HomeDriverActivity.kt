package com.charlye934.uber.home.presentation.ui.driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.charlye934.uber.R
import com.charlye934.uber.databinding.ActivityHomeDriverBinding
import com.charlye934.uber.home.presentation.listener.HomeListener
import com.charlye934.uber.login.presentation.LoginActivity
import com.charlye934.uber.providers.AuthProvider

class HomeDriverActivity : AppCompatActivity(), HomeListener {

    private lateinit var binding: ActivityHomeDriverBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewDriver) as NavHostFragment
        navController = navHostFragment.navController

        setUpActionBar()
    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.toolbar.toolbar)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mapDriverFragment)
        )
        binding.toolbar.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun logOutSession() {
        AuthProvider.logout()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}