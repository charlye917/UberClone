package com.charlye934.uber.login.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.charlye934.uber.R
import com.charlye934.uber.databinding.FragmentProfilingBinding
import com.charlye934.uber.databinding.FragmentSelectOptionAuthBinding
import com.charlye934.uber.login.presentation.navigator.UberLoginNavigateImp
import com.charlye934.uber.login.presentation.navigator.UberLoginNavigation

class ProfilingFragment : Fragment() {

    private lateinit var navigate: UberLoginNavigation
    private lateinit var binding: FragmentProfilingBinding
    private lateinit var mPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPref = requireContext().getSharedPreferences("typeUser", Context.MODE_PRIVATE)
        editor = mPref.edit()

        navigate = UberLoginNavigateImp(view)
        setOnclickListener()
    }

    private fun setOnclickListener(){
        binding.btnIAmClient.setOnClickListener {
            editor.putString("user", "client").apply()
            navigate.navigateToSelectAuth()
        }
        binding.btnIAmDriver.setOnClickListener {
            editor.putString("user", "driver").apply()
            navigate.navigateToSelectAuth()
        }
    }
}