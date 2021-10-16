package com.charlye934.uber.login.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.charlye934.uber.R
import com.charlye934.uber.databinding.FragmentRegisterBinding
import com.charlye934.uber.utils.isValidConfirmPassword
import com.charlye934.uber.utils.isValidateEmail
import com.charlye934.uber.utils.isValidatePassword
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        validateData()
    }

    private fun validateData(){
        val email = binding.textInputEmail.text.toString()
        val password = binding.textInputPassword.text.toString()
        val confirmPassword = binding.textInputPasswordValidate.text.toString()

        if(isValidateEmail(email, binding.textInputLayoutEmail) && isValidatePassword(password, binding.textInputLayoutPassword) && isValidConfirmPassword(password, confirmPassword, requireContext()))
            createAccount(email, password)

    }

    private fun createAccount(email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    mAuth.currentUser!!.sendEmailVerification()
                }
            }
    }

}