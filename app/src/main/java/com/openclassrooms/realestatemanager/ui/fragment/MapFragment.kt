package com.openclassrooms.realestatemanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.base.BaseFragment
import com.openclassrooms.realestatemanager.databinding.FragmentEstateMapBinding
import com.openclassrooms.realestatemanager.model.EstateUI
import com.openclassrooms.realestatemanager.util.MapUtils.getMapStyle
import com.openclassrooms.realestatemanager.util.MapUtils.navigateTo
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MapFragment : BaseFragment<FragmentEstateMapBinding?>(), OnMapReadyCallback {

    private val mainViewModel: MainViewModel by sharedViewModel()

    private var myGoogleMap: GoogleMap? = null

    private val markers: MutableList<Marker> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstateMapBinding.inflate(layoutInflater)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView(savedInstanceState)
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        binding?.apply {
            mapMapView.onCreate(savedInstanceState)
            mapMapView.getMapAsync(this@MapFragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myGoogleMap = googleMap

        // Depending on the phone's settings, dark mode is used for the map
        googleMap.setMapStyle(getMapStyle(requireContext()))

        // Refresh the location used by the MainViewModel when camera is idle
        myGoogleMap?.apply {
            setOnCameraIdleListener {
                /*restaurantViewModel.setLocation(
                    cameraPosition.target.latitude,
                    .cameraPosition.target.longitude
                )*/
            }
        }

        setUpListenersAndObservers()
    }

    // Refresh the list of markers displayed on the map
    private fun refreshMarkers(estates: List<EstateUI>?) {
        // Delete old markers
        for (marker in markers) {
            marker.remove()
        }
        if (estates != null) {
            // Create a new marker for each estate
            for (estate in estates) {
                if ((estate.estate.xCoordinate != null) && (estate.estate.yCoordinate != null)) {
                    myGoogleMap!!.addMarker(
                        MarkerOptions()
                            .position(
                                LatLng(
                                    estate.estate.xCoordinate!!,
                                    estate.estate.yCoordinate!!
                                )
                            )
                            .title(estate.estate.title)
                    )?.let {
                        markers.add(it)
                    }
                }
            }
        }
    }

    // Setup listeners and observers of MapFragment
    private fun setUpListenersAndObservers() {
        // Set an observer to get Estate
        mainViewModel.estates.observe(this) { estates: List<EstateUI> ->
            refreshMarkers(estates)
        }

        // Set an observer to get current location
        mainViewModel.coordinates.observe(this) { coordinates ->
            myGoogleMap?.navigateTo(coordinates)
        }
    }

    override fun onStart() {
        super.onStart()
        binding?.apply { mapMapView.onStart() }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply { mapMapView.onResume() }
    }

    override fun onPause() {
        super.onPause()
        binding?.apply { mapMapView.onPause() }
    }
}