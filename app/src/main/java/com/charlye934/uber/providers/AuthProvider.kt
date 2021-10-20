package com.charlye934.uber.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

object AuthProvider {
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String): Task<AuthResult>{
        return mAuth.createUserWithEmailAndPassword(email, password)
    }

    fun login(email: String, password: String): Task<AuthResult>{
        return mAuth.signInWithEmailAndPassword(email, password)
    }

    fun logout(){ mAuth.signOut() }

    fun getId(): String = mAuth.currentUser!!.uid

    fun exitSession(): Boolean{
        var exit = false
        if(mAuth.currentUser != null)
            exit = true
        return exit
    }

}