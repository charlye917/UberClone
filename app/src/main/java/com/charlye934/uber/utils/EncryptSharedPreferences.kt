package com.charlye934.uber.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EncryptSharedPreferences(val context: Context) {
    private val sharedPrefsFile: String = "secret_shared_prefs"
    private val masterKeysAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences
        .create(
            sharedPrefsFile,
            masterKeysAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun setData(typeUser: String){
        sharedPreferences.edit()
            .putString("TYPE_USER", typeUser)
            .apply()
    }

    fun redValue(): String{
        return sharedPreferences.getString("TYPE_USER", "") ?: ""
    }
}