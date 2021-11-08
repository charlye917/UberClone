package com.charlye934.uber.home.presentation.ui.client

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Camera
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.charlye934.uber.R
import com.charlye934.uber.databinding.FragmentMapClientBinding
import com.charlye934.uber.home.presentation.BaseHomeFragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions

class MapClientFragment : BaseHomeFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocation: FusedLocationProviderClient
    private lateinit var binding: FragmentMapClientBinding

    private var mCurrentLatLng: LatLng? = null
    private var mIsFirstTime = true

    private val mLocationCallback: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            for(location in locationResult.locations){
                mCurrentLatLng = LatLng(location.latitude, location.longitude)
                //Obtener la localizacion en tiempo real
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder()
                        .target(LatLng(location.latitude, location.longitude))
                        .zoom(15f)
                        .build()
                ))

                if(mIsFirstTime){
                    mIsFirstTime = false
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.mapGoogle.onCreate(savedInstanceState)
        binding.mapGoogle.onResume()

        binding.mapGoogle.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentMapClientBinding.inflate(layoutInflater)
        mFusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())//inicair o deterner la ubicacion del cliente

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun onPermissionResult(){
        var requestPerssion = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            when{
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ->{
                    requestLocation()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) ->{
                    requestLocation()
                }
                else ->{
                    Log.d("_tag","sin accesso")
                }
            }
        }

        requestPerssion.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    private fun requestLocation(){
        if(gpsActived()){
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
        }else{

        }
    }

    private fun gpsActived(): Boolean{
        var isActive = false
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            isActive = true
        }
        return isActive
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        //mMap.setOnCameraIdleListener()

        mLocationRequest = LocationRequest
            .create().apply {
                interval = 1000
                fastestInterval = 1000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                smallestDisplacement = 5f
            }

        onPermissionResult()
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
}