package com.charlye934.uber.login.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.charlye934.uber.R
import com.charlye934.uber.databinding.ActivityLoginBinding
import com.charlye934.uber.home.presentation.ui.client.HomeClientActivity
import com.charlye934.uber.login.presentation.listener.UberLoginNavigation

class LoginActivity : AppCompatActivity(), UberLoginNavigation {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        configToolbar()
    }

    private fun configToolbar(){
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        setSupportActionBar(binding.generciToolbar.toolbar)
        //setupActionBarWithNavController(navController)
        binding.generciToolbar.toolbar.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun navigateProfilingToLogin(view: View) {
        view.findNavController().navigate(R.id.action_profilingFragment_to_loginFragment)
    }

    override fun navigateLoginToRegister(view: View) {
        view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    override fun navigateRegisterToLogin(view: View) {
        view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    override fun goToMap(destination: Class<*>) {
        val intent = Intent(this, destination)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}