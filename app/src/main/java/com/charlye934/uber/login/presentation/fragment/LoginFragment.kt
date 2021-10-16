package com.charlye934.uber.login.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.charlye934.uber.R
import com.charlye934.uber.databinding.FragmentLoginBinding
import com.charlye934.uber.utils.FirebaseInstances
import com.charlye934.uber.utils.isValidateEmail
import com.charlye934.uber.utils.isValidatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var mAuth: FirebaseAuth = FirebaseInstances.mAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnclickListener()

    }

    private fun setOnclickListener() {
        val email = binding.textInputEmail.text.toString()
        val password = binding.textInputPassword.text.toString()

        binding.btnIngresar.setOnClickListener { loginByEmail(email, password) }
    }

    private fun loginByEmail(email: String, password:String){
        if(isValidateEmail(email, binding.textInputLayoutEmail) && isValidatePassword(password, binding.textInputLayoutPassword)) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (mAuth.currentUser!!.isEmailVerified) {
                            Log.d("__tag", "entro")
                        } else {
                            Toast.makeText(context, "User must confirm email first", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(context, "An unexpected error ocurred, please try again", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}