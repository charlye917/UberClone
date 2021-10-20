package com.charlye934.uber.login.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.charlye934.uber.databinding.FragmentRegisterBinding
import com.charlye934.uber.login.data.model.Client
import com.charlye934.uber.providers.AuthProvider
import com.charlye934.uber.providers.ClientProvider
import com.charlye934.uber.utils.isValidConfirmPassword
import com.charlye934.uber.utils.isValidateEmail
import com.charlye934.uber.utils.isValidatePassword
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val mAuthProvider =  AuthProvider
    private val mClientProvider = ClientProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnclickLister()
    }

    private fun setOnclickLister(){
        binding.btnRegister.setOnClickListener { validateData() }
    }

    private fun validateData(){
        val name = binding.textInputName.text.toString()
        val email = binding.textInputEmail.text.toString()
        val password = binding.textInputPassword.text.toString()
        val confirmPassword = binding.textInputPasswordValidate.text.toString()

        if(isValidateEmail(email, binding.textInputLayoutEmail) && isValidatePassword(password, binding.textInputLayoutPassword) && isValidConfirmPassword(password, confirmPassword, requireContext()))
            resgistrer(name, email, password)
        else
            Toast.makeText(context, "Ingrese los campos correctamente", Toast.LENGTH_SHORT).show()
    }

    private fun resgistrer(name: String, email: String, password: String){
        mAuthProvider.register(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val id = FirebaseAuth.getInstance().currentUser!!.uid
                    val client = Client(id, name, email)
                    createAccount(client)
                }
            }
    }

    private fun createAccount(client: Client){
        mClientProvider.create(client)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(context, "registro exitoso", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(
                        context,
                        "No se pudo crear el cliente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}