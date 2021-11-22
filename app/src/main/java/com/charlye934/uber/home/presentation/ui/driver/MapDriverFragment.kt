package com.charlye934.uber.home.presentation.ui.driver

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.charlye934.uber.R
import com.charlye934.uber.databinding.FragmentMapDriverBinding
import com.charlye934.uber.home.presentation.BaseHomeFragment
import com.charlye934.uber.providers.AuthProvider
import com.charlye934.uber.providers.GeoFireProvider
import com.charlye934.uber.utils.AlertDialogGeneric
import com.charlye934.uber.utils.Constants
import com.charlye934.uber.utils.SettingsLocations
import com.charlye934.uber.utils.openAppSettings
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.TokenProvider
import com.google.firebase.ktx.Firebase


class MapDriverFragment : BaseHomeFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap

    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mCurrentLatLng : LatLng
    private lateinit var mFusedLocation: FusedLocationProviderClient
    private var mMarker: Marker? = null

    private lateinit var binding: FragmentMapDriverBinding
    private lateinit var settingsLocations: SettingsLocations

    private var isConnected: Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.mapGoogle.onCreate(savedInstanceState)
        binding.mapGoogle.onResume()
        binding.mapGoogle.getMapAsync(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binding = FragmentMapDriverBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        askForPermissions()
        setOnclickListener()
    }

    @SuppressLint("MissingPermission")
    private fun askForPermissions(){
        settingsLocations = SettingsLocations(this)
        settingsLocations.runWithPermission{ startConnection() }
    }



    private fun setOnclickListener(){
        binding.btnConecte.setOnClickListener {
            if(isConnected) disconnected() else startConnection()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startConnection(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            isConnected = true
            binding.btnConecte.text = "Desconectarse"
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            mMap.isMyLocationEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    private fun disconnected(){
        if (mFusedLocation != null) {
            binding.btnConecte.text = "Conectarse"
            isConnected = false
            mMap.isMyLocationEnabled = false
            mFusedLocation.removeLocationUpdates(mLocationCallback)
            if(AuthProvider.exitSession())
                GeoFireProvider.removeLocation(AuthProvider.getId())

        } else {
            Toast.makeText(requireContext(), "No se puede desconectar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun instanceVariables(){
        mFusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())

        mLocationRequest = LocationRequest
            .create()
            .apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 5F
        }

        mLocationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                for(location in locationResult.locations){
                    if(context != null){
                        mCurrentLatLng = LatLng(location.latitude, location.longitude)
                        createMarker()
                        moveCamera()
                        updateLocation()
                    }
                }
            }
        }
    }

    private fun createMarker(){
        mMarker?.remove()
        mMarker = mMap.addMarker(MarkerOptions()
            .position(mCurrentLatLng)
            .title("Posicion actual")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.vehicles))
        )
    }

    private fun moveCamera(){
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
            CameraPosition.Builder()
                .target(LatLng(mCurrentLatLng.latitude, mCurrentLatLng.longitude))
                .zoom(15f)
                .build()
        ))
    }

    private fun updateLocation(){
        if(AuthProvider.exitSession() && mCurrentLatLng != null){
            GeoFireProvider.saveLocation(AuthProvider.getId(), mCurrentLatLng)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            instanceVariables()
            mMap = googleMap
            mMap.apply {
                mapType = GoogleMap.MAP_TYPE_NORMAL
                uiSettings.isZoomControlsEnabled = true
                setOnMarkerClickListener(this@MapDriverFragment)
            }

            startConnection()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.uber_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_history -> {return true}
            R.id.action_update -> {return true}
            R.id.action_logout -> {
                homeListener.logOutSession()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMarkerClick(p0: Marker): Boolean = false
}