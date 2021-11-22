package com.charlye934.uber.utils

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class SettingsLocations(
    fragment: Fragment,
) {

    private var onGranted: () -> Unit = {}
    private val perrmissionOnLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permission ->
            when{
                permission[Manifest.permission.ACCESS_FINE_LOCATION]!!
                        || permission[Manifest.permission.ACCESS_COARSE_LOCATION]!!-> {
                    if(gpsActived(fragment.requireContext()))
                        onGranted
                    else
                        AlertDialogGeneric.alertPermissionDenegate(
                            fragment.requireContext(),
                            Constants.ALERT_GPS
                        ){
                            fragment.requireContext().openAppSettings()
                        }
                }else -> {
                    AlertDialogGeneric.alertPermissionDenegate(
                        fragment.requireContext(),
                        Constants.ALERT_PERMISSION
                    ){
                        fragment.requireContext().openAppSettings()
                    }
                }
            }
        }

    fun runWithPermission(body: () -> Unit){
        onGranted = body
        perrmissionOnLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    fun gpsActived(context: Context): Boolean{
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}