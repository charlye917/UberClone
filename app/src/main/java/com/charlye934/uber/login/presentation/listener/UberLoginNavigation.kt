package com.charlye934.uber.login.presentation.listener

import android.view.View

interface UberLoginNavigation {
    fun navigateProfilingToLogin(view: View)
    fun navigateLoginToRegister(view: View)
    fun navigateRegisterToLogin(view: View)
    fun goToMap(destination: Class<*>)
}