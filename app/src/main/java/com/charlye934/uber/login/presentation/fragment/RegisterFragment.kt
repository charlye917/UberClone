package com.charlye934.uber.login.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.charlye934.uber.databinding.FragmentRegisterBinding
import com.charlye934.uber.login.data.model.Client
import com.charlye934.uber.login.data.model.Driver
import com.charlye934.uber.login.presentation.BaseLoginFragment
import com.charlye934.uber.login.presentation.listener.UberLoginNavigation
import com.charlye934.uber.providers.AuthProvider
import com.charlye934.uber.providers.ClientProvider
import com.charlye934.uber.providers.DriverProvider
import com.charlye934.uber.utils.*
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : BaseLoginFragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val mAuthProvider =  AuthProvider
    private val mClientProvider = ClientProvider
    private val mDriverProvider = DriverProvider
    private lateinit var typeUser: String
    private lateinit var sharedPreferences: EncryptSharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedPreferences = EncryptSharedPreferences(requireContext())
        typeUser = sharedPreferences.redValue()

        setOnclickLister()
        setUpView()
    }

    private fun setOnclickLister(){
        binding.btnRegister.setOnClickListener { validateData() }
    }

    private fun setUpView(){
        if(typeUser == Constants.USER){
            binding.textInputLayoutMarca.visibility = View.GONE
            binding.textInputLayoutPlate.visibility = View.GONE
        }else{
            binding.textInputLayoutMarca.visibility = View.VISIBLE
            binding.textInputLayoutPlate.visibility = View.VISIBLE
        }
    }

    private fun validateData(){
        val name = binding.textInputName.text.toString()
        val email = binding.textInputEmail.text.toString()
        val marca = binding.textInputMarca.text.toString()
        val placas = binding.textInputVehiclePlate.text.toString()
        val password = binding.textInputPassword.text.toString()
        val confirmPassword = binding.textInputPasswordValidate.text.toString()

        if(name.isNotEmpty() &&
            isValidateEmail(email, binding.textInputLayoutEmail) &&
            isValidatePassword(password, binding.textInputLayoutPassword) &&
            isValidConfirmPassword(password, confirmPassword, requireContext())){
            if(typeUser == Constants.USER){
                registrer(name, email, password)
            }else if(typeUser == Constants.DRIVER){
                if(validateDataNotEmpty(marca, binding.textInputLayoutMarca) &&
                    validateDataNotEmpty(placas, binding.textInputLayoutPlate)) {
                    registrer(name, email, password, marca, placas)
                }
            }
        }
        else {
            Toast.makeText(context, "Ingrese los campos correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registrer(name: String, email: String, password: String, marca: String = "", placas: String = ""){
        mAuthProvider.register(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val id = FirebaseAuth.getInstance().currentUser!!.uid
                    if(typeUser == Constants.USER){
                        val client = Client(id, name, email)
                        createAccountUser(client)
                    }else{
                        val driver = Driver(id, name, email, marca, placas)
                        createAccountDriver(driver)
                    }
                }
            }
    }

    private fun createAccountUser(client: Client){
        mClientProvider.create(client)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(context, "registro exitoso", Toast.LENGTH_SHORT).show()
                    uberLoginNavigation.navigateRegisterToLogin(requireView())
                }else{
                    Toast.makeText(context, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createAccountDriver(driver: Driver){
        mDriverProvider.create(driver)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(context, "registro exitoso", Toast.LENGTH_SHORT).show()
                    uberLoginNavigation.navigateRegisterToLogin(requireView())
                }else{
                    Toast.makeText(context, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show()
                }
            }
    }
}