package com.charlye934.uber.utils

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

fun isValidateEmail(email: String, inputLayout: TextInputLayout? = null): Boolean{
    val pattern = Patterns.EMAIL_ADDRESS
    Log.d("__tag"," $email ${pattern}")

    return if(pattern.matcher(email).matches()){
        inputLayout?.error = null
        true
    }else{
        inputLayout?.error = "FAVOR DE INGRESAR UN EMAIL VALIDO"
        false
    }
}

fun isValidatePassword(password: String, inputLayout: TextInputLayout? = null): Boolean{
    // Necesita Contener -->    1 Num / 1 Minuscula / 1 Mayuscula / 1 Special / Min Caracteres 4
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
    val pattern = Pattern.compile(passwordPattern)
    return if(pattern.matcher(password).matches()){
        inputLayout!!.error = null
        true
    }else{
        inputLayout?.error = "FAVOR DE INGRESAR UN PASSWORD VALIDO"
        false
    }
}

fun isValidConfirmPassword(password: String, confirmPassword: String, context: Context): Boolean{
    return if( password == confirmPassword) true
    else{
        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        false
    }
}

fun validateDataNotEmpty(data: String, inputLayout: TextInputLayout? = null): Boolean{
    return if(data.isNotEmpty()){
        inputLayout!!.error = null
        true
    }else{
        inputLayout!!.error = "FAVOR DE LLENAR LOS CAMPOS"
        false
    }
}