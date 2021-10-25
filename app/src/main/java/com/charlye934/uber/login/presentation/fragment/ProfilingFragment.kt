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
import com.charlye934.uber.utils.EncryptSharedPreferences

class ProfilingFragment : Fragment() {

    private lateinit var navigate: UberLoginNavigation
    private lateinit var binding: FragmentProfilingBinding
    private lateinit var sharedPreferences: EncryptSharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = EncryptSharedPreferences(requireContext())
        navigate = UberLoginNavigateImp(view)
        setOnclickListener()
    }

    private fun setOnclickListener(){
        binding.btnIAmClient.setOnClickListener {
            sharedPreferences.setData("CLIENT")
            navigate.navigateToSelectAuth()
        }
        binding.btnIAmDriver.setOnClickListener {
            sharedPreferences.setData("DRIVER")
            navigate.navigateToSelectAuth()
        }
    }
}