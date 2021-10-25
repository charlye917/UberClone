package com.charlye934.uber.login.presentation.fragment

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.charlye934.uber.MainActivity
import com.charlye934.uber.R
import com.charlye934.uber.databinding.ActionBarToolbarBinding
import com.charlye934.uber.databinding.FragmentSelectOptionAuthBinding
import com.charlye934.uber.login.presentation.navigator.UberLoginNavigateImp
import com.charlye934.uber.login.presentation.navigator.UberLoginNavigation

class SelectOptionAuthFragment : Fragment() {

    private lateinit var navigate: UberLoginNavigation
    private lateinit var binding: FragmentSelectOptionAuthBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //setHasOptionsMenu(true)

        binding = FragmentSelectOptionAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigate = UberLoginNavigateImp(view)
        setOnclickListener()
    }

    private fun setOnclickListener(){
        binding.btnGoToLogin.setOnClickListener { navigate.navigateToLogin() }
        binding.btnGoToRegister.setOnClickListener { navigate.navigateToRegister() }
    }
}