package com.openclassrooms.realestatemanager.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.github.clans.fab.FloatingActionMenu
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityEstateDetailsBinding
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.ui.adapter.ListImageWithDescriptionAdapter
import com.openclassrooms.realestatemanager.util.MapUtils
import com.openclassrooms.realestatemanager.util.MapUtils.navigateTo
import com.openclassrooms.realestatemanager.viewModel.EstateDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class EstateDetailsActivity : BaseActivity<ActivityEstateDetailsBinding>(), OnMapReadyCallback, ListImageWithDescriptionAdapter.OnItemClick {

    private val estateDetailsViewModel: EstateDetailsViewModel by viewModel()

    private var map: GoogleMap? = null
    private var marker: Marker? = null

    private var mAdapter: ListImageWithDescriptionAdapter? = null

    private var estateId:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstateDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        initRecycler()
        initMapView(savedInstanceState)
        setUpListenersAndObservers()

        binding?.addEditEstateMore?.menuIconView?.setColorFilter(Color.BLACK)

        estateId = intent.getIntExtra("estate_id", -1)
        estateDetailsViewModel.setEstateId(estateId)
    }

    private fun initRecycler() {
        mAdapter = ListImageWithDescriptionAdapter(ArrayList(), this, this)
        binding!!.estateDetailsImageList.apply {
            layoutManager = LinearLayoutManager(this@EstateDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
    }

    private fun refreshRecycler(myList: List<ImageWithDescription>) {
        mAdapter!!.updateList(myList)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUpListenersAndObservers() {
        estateDetailsViewModel.estate.observe(this) { estate ->
            refreshRecycler(estate.images!!)
            binding!!.estateDetailsDescriptionText.text = estate.estate.description
            binding!!.estateDetailsArea.text = estate.estate.area.toString().plus("m²")
            binding!!.estateDetailsRooms.text = estate.estate.nbRooms.toString()
            binding!!.estateDetailsBedrooms.text = estate.estate.nbBedrooms.toString()
            binding!!.estateDetailsBathrooms.text = estate.estate.nbBathrooms.toString()
            binding!!.estateDetailsLocationAddress.text = estate.estate.getAddressInPresentation()
            if(estate.estate.nearbyShop == true) binding!!.estateDetailsIsShop.setImageDrawable(getDrawable(R.drawable.ic_done)) else binding!!.estateDetailsIsShop.setImageDrawable(getDrawable(R.drawable.ic_cross))
            if(estate.estate.nearbySchool == true) binding!!.estateDetailsIsSchool.setImageDrawable(getDrawable(R.drawable.ic_done)) else binding!!.estateDetailsIsSchool.setImageDrawable(getDrawable(R.drawable.ic_cross))
            if(estate.estate.nearbyPark == true) binding!!.estateDetailsIsPark.setImageDrawable(getDrawable(R.drawable.ic_done)) else binding!!.estateDetailsIsPark.setImageDrawable(getDrawable(R.drawable.ic_cross))
            map!!.navigateTo(Coordinates(estate.estate.xCoordinate!!, estate.estate.yCoordinate!!), 16f)
            if(marker != null) marker!!.remove()
            map!!.addMarker(
                MarkerOptions()
                    .position(LatLng(estate.estate.xCoordinate!!, estate.estate.yCoordinate!!))
            )?.let { marker = it }

            if(estate.estate.sold) {
                binding?.estateDetailsIsSoldImage?.load(R.drawable.sold)
                binding?.estateDetailsSoldButton?.labelText = getString(R.string.estate_details_edit_estate_button_sold_to_unsold)
            }
            else {
                binding?.estateDetailsIsSoldImage?.load(0x00000000)
                binding?.estateDetailsSoldButton?.labelText = getString(R.string.estate_details_edit_estate_button_sold_to_sold)
            }
        }

        binding?.estateDetailsEditButton?.setOnClickListener {
            MainActivity.navigateToAddEditActivity(this, estateId)
        }

        binding?.estateDetailsSoldButton?.setOnClickListener {
            estateDetailsViewModel.updateSoldState()
        }
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        binding?.apply {
            estateDetailsLiteMap.onCreate(savedInstanceState)
            estateDetailsLiteMap.getMapAsync(this@EstateDetailsActivity)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Depending on the phone's settings, dark mode is used for the map
        googleMap.setMapStyle(MapUtils.getMapStyle(this))    }

    override fun onClick(imageWithDescription: ImageWithDescription) {
        //TODO - Display the selected image in fullscreen
        TODO("Not yet implemented")
    }

}