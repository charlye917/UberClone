package com.charlye934.uber.login.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.charlye934.uber.R
import com.charlye934.uber.databinding.FragmentLoginBinding
import com.charlye934.uber.home.presentation.ui.client.HomeClientActivity
import com.charlye934.uber.home.presentation.ui.driver.HomeDriverActivity
import com.charlye934.uber.login.presentation.BaseLoginFragment
import com.charlye934.uber.login.presentation.listener.UberLoginNavigation
import com.charlye934.uber.utils.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : BaseLoginFragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth //= FirebaseInstances.mAuth
    private lateinit var loader: AlertDialogLoader
    private lateinit var sharedPreferences: EncryptSharedPreferences

    private val mGoogleClient by lazy{ getGoogleApiClient() }
    private val RC_CODE_SIIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = EncryptSharedPreferences(requireContext())
        loader = AlertDialogLoader(requireContext())
        setOnclickListener()
    }

    private fun setOnclickListener() {
        binding.btnIngresar.setOnClickListener {
            loader.showLoader()
            sigInLogin()
        }
        binding.btnRegister.setOnClickListener { uberLoginNavigation.navigateLoginToRegister(requireView()) }
        binding.btnLoginGoogle.setOnClickListener {
            loader.showLoader()
            val signIntent = mGoogleClient.signInIntent
            startActivityForResult(signIntent, RC_CODE_SIIGN_IN)
        }
    }

    private fun sigInLogin(){
        val email = binding.textInputEmail.text.toString().trim()
        val password = binding.textInputPassword.text.toString().trim()

        if(isValidateEmail(email, binding.textInputLayoutEmail) &&  isValidatePassword(password, binding.textInputLayoutPassword))
            loginByEmail(email, password)
        else {
            loader.hideLoader()
            Toast.makeText(context, "Favor de llenar los campos correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginByEmail(email: String, password:String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                loader.hideLoader()
                if (it.isSuccessful) {
                    determinateDestination()
                }else{
                    Toast.makeText(context, "An unexpected error ocurred, please try again", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getGoogleApiClient(): GoogleSignInClient{
        val gson = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(requireContext(), gson)
    }

    private fun loginByGoogleAndFirebase(googleAccount: String){
        val credential = GoogleAuthProvider.getCredential(googleAccount, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                loader.hideLoader()
                if(it.isSuccessful){
                    if(GoogleSignIn.getLastSignedInAccount(requireContext()) != null){
                        determinateDestination()
                    }
                }else{
                    Toast.makeText(context, "Fallo al iniciar sesion", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun determinateDestination(){
        if(sharedPreferences.redValue() == Constants.USER)
            uberLoginNavigation.goToMap(HomeClientActivity::class.java)
        else
            uberLoginNavigation.goToMap(HomeDriverActivity::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_CODE_SIIGN_IN){
            val credential = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = credential.getResult(ApiException::class.java)
                loginByGoogleAndFirebase(account.idToken!!)
            }catch (e: ApiException){
                Toast.makeText(context, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}