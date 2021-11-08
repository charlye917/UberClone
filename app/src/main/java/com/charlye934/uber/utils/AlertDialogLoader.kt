package com.charlye934.uber.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.charlye934.uber.R

class AlertDialogLoader(context: Context) {

    private val layoutBuilder = LayoutInflater.from(context).inflate(R.layout.lottie_load, null)
    private val builder = AlertDialog.Builder(context).setView(layoutBuilder)
    private val alertDialog = builder.create()

    fun showLoader() {
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun hideLoader(){
        alertDialog.hide()
    }
}