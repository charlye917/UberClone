package com.charlye934.uber.login.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.charlye934.uber.databinding.FragmentProfilingBinding
import com.charlye934.uber.home.presentation.ui.client.HomeClientActivity
import com.charlye934.uber.home.presentation.ui.driver.HomeDriverActivity
import com.charlye934.uber.login.presentation.BaseLoginFragment
import com.charlye934.uber.login.presentation.listener.UberLoginNavigation
import com.charlye934.uber.utils.AlertDialogLoader
import com.charlye934.uber.utils.Constants
import com.charlye934.uber.utils.EncryptSharedPreferences
import com.google.firebase.auth.FirebaseAuth

class ProfilingFragment : BaseLoginFragment() {

    private lateinit var binding: FragmentProfilingBinding
    private lateinit var sharedPreferences: EncryptSharedPreferences
    private lateinit var loader: AlertDialogLoader


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader = AlertDialogLoader(requireContext())
        checkSession()
    }

    private fun checkSession(){
        sharedPreferences = EncryptSharedPreferences(requireContext())
        //loader.showLoader()
        if(FirebaseAuth.getInstance().currentUser != null){
            //loader.hideLoader()
            if(sharedPreferences.redValue() == Constants.USER)
                uberLoginNavigation.goToMap(HomeClientActivity::class.java)
            else
                uberLoginNavigation.goToMap(HomeDriverActivity::class.java)
        }else{
            //loader.hideLoader()
            setOnclickListener()
        }
    }

    private fun setOnclickListener(){
        binding.btnIAmClient.setOnClickListener {
            sharedPreferences.setData(Constants.USER)
            uberLoginNavigation.navigateProfilingToLogin(it)
        }
        binding.btnIAmDriver.setOnClickListener {
            sharedPreferences.setData(Constants.DRIVER)
            uberLoginNavigation.navigateProfilingToLogin(it)
        }
    }
}