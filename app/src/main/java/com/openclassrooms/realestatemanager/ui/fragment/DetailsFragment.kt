package com.openclassrooms.realestatemanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseFragment
import com.openclassrooms.realestatemanager.databinding.FragmentEstateDetailsBinding
import com.openclassrooms.realestatemanager.model.Coordinates
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.ui.adapter.ListImageWithDescriptionAdapter
import com.openclassrooms.realestatemanager.util.MapUtils
import com.openclassrooms.realestatemanager.util.MapUtils.navigateTo
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class DetailsFragment : BaseFragment<FragmentEstateDetailsBinding>(), KoinComponent, OnMapReadyCallback, ListImageWithDescriptionAdapter.OnItemClick {

    private var mAdapter: ListImageWithDescriptionAdapter? = null

    private val estateDetailsViewModel: MainViewModel by sharedViewModel()

    private var map: GoogleMap? = null
    private var marker: Marker? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEstateDetailsBinding.inflate(layoutInflater)

        initMapView(savedInstanceState)
        initRecycler()

        return binding!!.root
    }

    private fun setUpListenersAndObservers() {
        estateDetailsViewModel.estate.observe(viewLifecycleOwner) { estate ->
            refreshRecycler(estate.images)
            binding?.estateDetailsDescriptionText?.text = estate.estate.description
            binding?.estateDetailsArea?.text = estate.estate.area.toString().plus("m²")
            binding?.estateDetailsRooms?.text = estate.estate.nbRooms.toString()
            binding?.estateDetailsBedrooms?.text = estate.estate.nbBedrooms.toString()
            binding?.estateDetailsBathrooms?.text = estate.estate.nbBathrooms.toString()
            binding?.estateDetailsLocationAddress?.text = estate.estate.getAddressInPresentation()
            if(estate.estate.nearbyShop == true)
                binding?.estateDetailsIsShop?.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_done))
            else binding?.estateDetailsIsShop?.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_cross))
            if(estate.estate.nearbySchool == true)
                binding?.estateDetailsIsSchool?.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_done))
            else binding?.estateDetailsIsSchool?.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_cross))
            if(estate.estate.nearbyPark == true)
                binding?.estateDetailsIsPark?.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_done))
            else binding?.estateDetailsIsPark?.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_cross))
            map!!.navigateTo(Coordinates(estate.estate.xCoordinate!!, estate.estate.yCoordinate!!), 16f)
            if(marker != null) marker!!.remove()
            map!!.addMarker(
                MarkerOptions()
                    .position(LatLng(estate.estate.xCoordinate!!, estate.estate.yCoordinate!!))
            )?.let { marker = it }

            if(estate.estate.sold) {
                binding?.estateDetailsIsSoldImage?.load(R.drawable.sold)
                binding?.estateDetailsSoldButton?.title = getString(R.string.estate_details_edit_estate_button_sold_to_unsold)
            }
            else {
                binding?.estateDetailsIsSoldImage?.load(0x00000000)
                binding?.estateDetailsSoldButton?.title = getString(R.string.estate_details_edit_estate_button_sold_to_sold)
            }
        }

        binding?.estateDetailsSoldButton?.setOnClickListener {
            estateDetailsViewModel.updateSoldState()
        }
    }

    private fun initRecycler() {
        mAdapter = ListImageWithDescriptionAdapter(ArrayList(), requireContext(), this)
        binding!!.estateDetailsImageList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
    }

    private fun refreshRecycler(myList: List<ImageWithDescription>) {
        mAdapter!!.updateList(myList)
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        binding?.apply {
            estateDetailsLiteMap.onCreate(savedInstanceState)
            estateDetailsLiteMap.getMapAsync(this@DetailsFragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Depending on the phone's settings, dark mode is used for the map
        googleMap.setMapStyle(MapUtils.getMapStyle(requireContext()))
        setUpListenersAndObservers()
    }

    override fun onImageClick(imageWithDescription: ImageWithDescription) {
        //TODO - Display the selected image in fullscreen
        TODO("Not yet implemented")
    }

}