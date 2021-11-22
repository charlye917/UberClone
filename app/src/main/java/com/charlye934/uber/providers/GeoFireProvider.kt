package com.charlye934.uber.providers

import android.database.DatabaseErrorHandler
import android.util.Log
import com.charlye934.uber.utils.Constants
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object GeoFireProvider {
    private val mDatabaseReferences= FirebaseDatabase.getInstance().reference.child("Users").child(Constants.DRIVER_LOCATIONS)
    private var mGeoFire: GeoFire = GeoFire(mDatabaseReferences)

    fun saveLocation(idDriver: String, latLng: LatLng){
        mGeoFire.setLocation(
            idDriver,
            GeoLocation(latLng.latitude, latLng.longitude)
        ) { key, error ->
            Log.d("__tag", "$key, $error")
        }
    }

    fun removeLocation(idDriver: String){
        mGeoFire.removeLocation(idDriver)
    }

    fun getActiveDrivers(latLng: LatLng, radius: Double): GeoQuery{
        val geoQuery = mGeoFire.queryAtLocation(GeoLocation(latLng.latitude, latLng.longitude), radius)
        Log.d("__tag getactive","$geoQuery, $latLng")
        geoQuery.removeAllListeners()
        return geoQuery
    }

    fun getDriverslocation(idDriver: String): DatabaseReference{
        return mDatabaseReferences.child(idDriver).child("l")
    }

    fun isDriverWorking(idDriver: String): DatabaseReference{
        return FirebaseDatabase.getInstance().reference.child("driver_working").child(idDriver)
    }


}