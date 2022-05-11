package com.openclassrooms.realestatemanager.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityAddEditEstateBinding
import com.openclassrooms.realestatemanager.util.MapUtils.getMapStyle
import com.openclassrooms.realestatemanager.util.MapUtils.navigateTo
import com.openclassrooms.realestatemanager.viewModel.AddEditEstateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent


class AddEditEstateActivity : BaseActivity<ActivityAddEditEstateBinding>(), KoinComponent, OnMapReadyCallback {

    private val addEditEstateViewModel: AddEditEstateViewModel by viewModel()

    private var map: GoogleMap? = null
    private var marker: Marker? = null
    var options = GoogleMapOptions().liteMode(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_estate)

        binding = ActivityAddEditEstateBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setApiKey()
        setUpListenersAndObservers()
        initMapView(savedInstanceState)

    }

    private fun setUpListenersAndObservers() {

        // TextField for the Estate's country
        binding?.addEditEstateCountry?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setCountry(s.toString())
            }
        })

        // TextField for the Estate's city
        binding?.addEditEstateCity?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setCity(s.toString())
            }
        })

        // TextField for the Estate's zip code
        binding?.addEditEstateZip?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setZip(s.toString())
            }
        })

        // TextField for the Estate's address
        binding?.addEditEstateAddress?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setAddress(s.toString())
            }
        })

        // Click listener to find the Estate's location
        binding?.addEditEstateFind?.setOnClickListener {
            addEditEstateViewModel.searchLocation()
        }

        // Click listener to change currency
        binding?.addEditEstateCurrency?.setOnClickListener {
            addEditEstateViewModel.changeCurrency()
        }

        // Click listener to add a picture
        binding?.addEditEstateAddImageCardView?.setOnClickListener {
            selectImage()
        }

        // Observe the Estate's coordinates
        addEditEstateViewModel.coordinates.observe(this) { coordinates ->
            map?.navigateTo(coordinates, 16f)
            if(marker != null) marker!!.remove()
            map!!.addMarker(
                MarkerOptions()
                    .position(LatLng(coordinates.xCoordinate, coordinates.yCoordinate))
            )?.let { marker = it }
        }

        // Observe the used currency
        addEditEstateViewModel.isDollar.observe(this) { isDollar ->
            if(isDollar) binding?.addEditEstateCurrency?.text = "$"
            else binding?.addEditEstateCurrency?.text = "€"
        }
    }

    private fun setApiKey() {
        if(!addEditEstateViewModel.isPositionStackApiKeyDefined()) addEditEstateViewModel.setPositionStackApiKey(getString(R.string.position_stack_api_key))
    }

    override fun onStart() {
        super.onStart()
        binding?.apply { addEditEstateLiteMap.onStart() }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply { addEditEstateLiteMap.onResume() }
    }

    override fun onPause() {
        super.onPause()
        binding?.apply { addEditEstateLiteMap.onPause() }
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        binding?.apply {
            addEditEstateLiteMap.onCreate(savedInstanceState)
            addEditEstateLiteMap.getMapAsync(this@AddEditEstateActivity)
        }
        //TODO - Set get localisation button
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Depending on the phone's settings, dark mode is used for the map
        googleMap.setMapStyle(getMapStyle(this))

        /*map!!.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )  */

    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>(getString(R.string.add_edit_estate_save_take_picture_item), getString(R.string.add_edit_estate_save_from_gallery_item))
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.add_edit_estate_save_choose_image_title))
        builder.setItems(options) { _, item ->
            if (options[item] == getString(R.string.add_edit_estate_save_take_picture_item)) takePicture()
            if (options[item] == getString(R.string.add_edit_estate_save_from_gallery_item)) chooseImageGallery()
        }
        builder.show()
    }

    private fun takePicture() {
        getResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getResult.launch(intent)

    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val value = it.data?.getStringExtra("input")
            }
        }

}