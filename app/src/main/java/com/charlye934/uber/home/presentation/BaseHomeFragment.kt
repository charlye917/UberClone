package com.charlye934.uber.home.presentation

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.charlye934.uber.home.presentation.listener.HomeListener
import java.lang.ClassCastException

abstract class BaseHomeFragment: Fragment() {

    lateinit var homeListener: HomeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            homeListener = context as HomeListener
        }catch (e: ClassCastException){
            Log.e("__tag", "$context must implement UICommunicationListener" )
        }
    }
}