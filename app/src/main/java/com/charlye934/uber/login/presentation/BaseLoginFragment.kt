package com.charlye934.uber.login.presentation

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.charlye934.uber.login.presentation.listener.UberLoginNavigation
import java.lang.ClassCastException

abstract class BaseLoginFragment: Fragment() {

    lateinit var uberLoginNavigation: UberLoginNavigation

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uberLoginNavigation = context as UberLoginNavigation
        }catch (e: ClassCastException){
            Log.e("__tag", "$context must implement UICommunicationListener" )
        }
    }
}