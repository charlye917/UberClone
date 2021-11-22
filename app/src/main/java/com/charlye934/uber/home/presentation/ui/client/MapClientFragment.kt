package com.charlye934.uber.home.presentation.ui.client

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import com.charlye934.uber.R
import com.charlye934.uber.databinding.FragmentMapClientBinding
import com.charlye934.uber.home.presentation.BaseHomeFragment
import com.charlye934.uber.providers.AuthProvider
import com.charlye934.uber.providers.GeoFireProvider
import com.charlye934.uber.utils.SettingsLocations
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DatabaseError

class MapClientFragment : BaseHomeFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap

    private  lateinit var mLocationCallback: LocationCallback
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mCurrentLatLng: LatLng
    private lateinit var mFusedLocation: FusedLocationProviderClient
    private var mMarker: Marker? = null
    private var mDriversMarkers = arrayListOf<Marker>()

    private lateinit var binding: FragmentMapClientBinding
    private lateinit var settingsLocations: SettingsLocations

    private var mIsFirstTime = true

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.mapGoogle.onCreate(savedInstanceState)
        binding.mapGoogle.onResume()
        binding.mapGoogle.getMapAsync(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentMapClientBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        askForPermissions()
    }

    private fun askForPermissions(){
        settingsLocations = SettingsLocations(this)
        settingsLocations.runWithPermission { startConnection() }
    }

    @SuppressLint("MissingPermission")
    private fun startConnection() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            mMap.isMyLocationEnabled = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            instanceVariables()
            mMap = googleMap

            mMap.apply {
                mapType = GoogleMap.MAP_TYPE_NORMAL
                uiSettings.isZoomControlsEnabled = true
                setOnMarkerClickListener(this@MapClientFragment)
            }

            startConnection()
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
                smallestDisplacement = 10F
            }

        mLocationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                for(location in locationResult.locations){
                    if(context != null){
                        mCurrentLatLng = LatLng(location.latitude, location.longitude)
                        createMarket()
                        moveCamera()

                        if(mIsFirstTime){
                            Log.d("__tag","entro fisttime")
                            mIsFirstTime = false
                            getActivityDrivers()
                            limitSearch()
                        }
                    }
                }
            }
        }
    }

    private fun createMarket(){
        mMarker?.remove()
        mMarker = mMap.addMarker(
            MarkerOptions().position(mCurrentLatLng).title("Conductor disponible")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location)))
    }

    private fun moveCamera(){
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
            CameraPosition.Builder()
                .target(LatLng(mCurrentLatLng.latitude, mCurrentLatLng.longitude))
                .zoom(15f)
                .build()
        ))
    }

    private fun getActivityDrivers(){
        GeoFireProvider.getActiveDrivers(mCurrentLatLng, 100.0)
            .addGeoQueryEventListener(object : GeoQueryEventListener {
                // AÑADIREMOS LOS MARCADORES DE LOS CONDUCTORES QUE SE CONECTEN EN LA APLICACION
                override fun onKeyEntered(key: String, location: GeoLocation) {
                    for (marker in mDriversMarkers) {
                        if (marker.tag != null) {
                            if (marker.tag == key) {
                                return
                            }
                        }
                    }
                    val driverLatLng = LatLng(location.latitude, location.longitude)
                    val marker = mMap.addMarker(
                        MarkerOptions().position(driverLatLng).title("Conductor disponible")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.vehicles))
                    )
                    marker.tag = key
                    mDriversMarkers.add(marker)
                }

                //Eliminamos los marcadores de los conductores que se desconectan
                override fun onKeyExited(key: String) {
                    Log.d("__tag keyexit","$key")
                    for (marker in mDriversMarkers) {
                        if (marker.tag != null) {
                            if (marker.tag == key) {
                                marker.remove()
                                mDriversMarkers.remove(marker)
                                return
                            }
                        }
                    }
                }

                //Actualizaremos los marcadores cuando se mueva
                override fun onKeyMoved(key: String, location: GeoLocation) {
                    // ACTUALIZAR LA POSICION DE CADA CONDUCTOR
                    Log.d("__tag keymoved","$key $location")
                    for (marker in mDriversMarkers) {
                        if (marker.tag != null) {
                            if (marker.tag == key) {
                                marker.position = LatLng(location.latitude, location.longitude)
                            }
                        }
                    }
                }

                override fun onGeoQueryReady() {}
                override fun onGeoQueryError(error: DatabaseError) {}

            })
    }

    private fun limitSearch(){

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