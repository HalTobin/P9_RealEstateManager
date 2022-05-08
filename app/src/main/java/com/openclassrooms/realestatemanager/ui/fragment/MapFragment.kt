package com.openclassrooms.realestatemanager.ui.fragment

import android.Manifest
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseFragment
import com.openclassrooms.realestatemanager.databinding.FragmentEstateMapBinding
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.util.MapUtils.getMapStyle
import com.openclassrooms.realestatemanager.util.MapUtils.navigateTo
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent


class MapFragment : BaseFragment<FragmentEstateMapBinding?>(), KoinComponent, OnMapReadyCallback {

    private var mBinding: FragmentEstateMapBinding? = null
    private val mainViewModel: MainViewModel by sharedViewModel()

    private var myGoogleMap: GoogleMap? = null
    private var isPermissionGranted: Boolean = false

    private val markers: MutableList<Marker> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEstateMapBinding.inflate(
            layoutInflater
        )

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         initMapView(savedInstanceState)
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        mBinding?.apply {
            mapMapView.onCreate(savedInstanceState)
            mapMapView.getMapAsync(this@MapFragment)
        }
        //TODO - Set get localisation button
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myGoogleMap = googleMap

        // Depending on the phone's settings, dark mode is used for the map
        googleMap.setMapStyle(getMapStyle(requireContext()))

        // Refresh the location used by the MainViewModel when camera is idle
        myGoogleMap?.apply { setOnCameraIdleListener {
            /*restaurantViewModel.setLocation(
                cameraPosition.target.latitude,
                .cameraPosition.target.longitude
            )*/
        } }

        setUpListenersAndObservers()
    }

    private fun refreshMarkers(estates: List<Estate>?) {
        // Delete old markers
        for (marker in markers) {
            marker.remove()
        }
        if (estates != null) {
            // Create new markers
            for (estate in estates) {
                if((estate.xCoordinate != null) && (estate.yCoordinate != null)) {
                    myGoogleMap!!.addMarker(
                        MarkerOptions()
                            .position(LatLng(estate.xCoordinate!!, estate.yCoordinate!!))
                            .title(estate.title)
                    )?.let { markers.add(it) }
                }
            }
        }
    }

    // Check if the user have allow the app to access his location
    fun checkPermission() {
        Dexter.withContext(context).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                println("Permission granted")
                isPermissionGranted = true
                //TODO - Search user location
                //restaurantViewModel.searchUserLocation(context)
            }
            override fun onPermissionRationaleShouldBeShown(
                list: List<PermissionRequest>,
                permissionToken: PermissionToken
            ) {
                isPermissionGranted = false
                permissionToken.continuePermissionRequest()
            }
        }).check()
    }

    // Setup listeners and observers of MapFragment
    private fun setUpListenersAndObservers() {
        // Set an observer to get Estate
        mainViewModel.getEstates().observe(this) { estates: List<Estate> ->
            refreshMarkers(estates)
        }
        /*getEstatesJob?.cancel()
        getEstatesJob = mainViewModel.getEstates().onEach {
            refreshMarkers(it)
        }.launchIn(mainViewModel.viewModelScope)*/

        // Set an observer to get current location
        mainViewModel.coordinates.observe(this) { coordinates ->
            myGoogleMap?.navigateTo(coordinates)
        }

        // Set a listener to refresh location
        mBinding?.apply { mapBtLocate.setOnClickListener {
            mainViewModel.findCurrentLocation(requireContext())
        } }
    }

    override fun onStart() {
        super.onStart()
        mBinding?.apply { mapMapView.onStart() }
    }

    override fun onResume() {
        super.onResume()
        mBinding?.apply { mapMapView.onResume() }
    }

    override fun onPause() {
        super.onPause()
        mBinding?.apply { mapMapView.onPause() }
    }
}