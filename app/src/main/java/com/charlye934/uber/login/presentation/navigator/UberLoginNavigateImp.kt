package com.charlye934.uber.login.presentation.navigator

import android.view.View
import androidx.navigation.findNavController
import com.charlye934.uber.R

class UberLoginNavigateImp(private val view: View) : UberLoginNavigation {
    override fun navigateToSelectAuth() {
        view.findNavController().navigate(R.id.action_profilingFragment_to_selectOptionAuthFragment)
    }

    override fun navigateToRegister() {
        view.findNavController().navigate(R.id.action_selectOptionAuthFragment_to_registerFragment)
    }

    override fun navigateToLogin() {
        view.findNavController().navigate(R.id.action_selectOptionAuthFragment_to_loginFragment)
    }
}