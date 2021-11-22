package com.charlye934.uber.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

object AlertDialogGeneric {

    fun alertPermissionDenegate(context: Context, type: Int,actionButtonPositive: () -> Unit){
        var title = ""
        var message = ""
        when(type){
            Constants.ALERT_PERMISSION -> {
                title =  "Proporciona los permisos para continuar"
                message = "Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse"
            }
            Constants.ALERT_GPS -> {
                title = ""
                message = "Por favor activa tu ubicacion para continuar"
            }
        }
        AlertDialog
            .Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar", DialogInterface.OnClickListener{ _, _ ->
                actionButtonPositive()
            })
            .setCancelable(false)
            .create()
            .show()
    }
}