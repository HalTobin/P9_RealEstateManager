package com.openclassrooms.realestatemanager.ui.fragment

import com.openclassrooms.realestatemanager.base.BaseFragment
import com.openclassrooms.realestatemanager.ui.adapter.ListEstatePagerAdapter
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import android.os.Bundle
import com.openclassrooms.realestatemanager.model.Estate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.databinding.FragmentEstateMapBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment : BaseFragment<FragmentEstateMapBinding?>(), KoinComponent {
    private var mBinding: FragmentEstateMapBinding? = null

    private val mainViewModel: MainViewModel by sharedViewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.getEstates().observe(this) { estates: List<Estate> ->
            //TODO
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEstateMapBinding.inflate(
            layoutInflater
        )

        mBinding!!.mapMapView.setUseDataConnection(true)
        mBinding!!.mapMapView.setTileSource(TileSourceFactory.MAPNIK)
        mBinding!!.mapMapView.setMultiTouchControls(true)
        mBinding!!.mapMapView.setBuiltInZoomControls(true)

        val startPoint = GeoPoint(43.65020, 7.00517)

        val mapController: IMapController
        mapController = mBinding!!.mapMapView.controller
        mapController.setCenter(startPoint)
        mapController.setZoom(14.1)

        val gpsMyLocationProvider = GpsMyLocationProvider(this.context)
        val myLocationOverlay = MyLocationNewOverlay(gpsMyLocationProvider, mBinding!!.mapMapView)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()

        return mBinding!!.root
    }

    override fun onResume() {
        super.onResume()
        mBinding!!.mapMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding!!.mapMapView.onPause()
    }

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
}