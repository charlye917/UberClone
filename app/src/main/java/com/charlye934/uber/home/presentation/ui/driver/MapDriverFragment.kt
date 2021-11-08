package com.charlye934.uber.home.presentation.ui.driver

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.charlye934.uber.R
import com.charlye934.uber.databinding.FragmentMapDriverBinding
import com.charlye934.uber.home.presentation.BaseHomeFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


class MapDriverFragment : BaseHomeFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapDriverBinding
    private lateinit var mMap: GoogleMap

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.mapGoogle.onCreate(savedInstanceState)
        binding.mapGoogle.onResume()
        binding.mapGoogle.getMapAsync(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentMapDriverBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
            CameraPosition.Builder()
                .target(LatLng(1.2222, -77.12222))
                .zoom(17f)
                .build()
        ))
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