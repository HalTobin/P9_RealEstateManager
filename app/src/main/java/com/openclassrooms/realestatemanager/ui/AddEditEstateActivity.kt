package com.openclassrooms.realestatemanager.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.internal.Objects
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityAddEditEstateBinding
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.ui.adapter.ListImageWithDescriptionAdapter
import com.openclassrooms.realestatemanager.util.MapUtils.getMapStyle
import com.openclassrooms.realestatemanager.util.MapUtils.navigateTo
import com.openclassrooms.realestatemanager.viewModel.AddEditEstateViewModel
import lib.android.imagepicker.ImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AddEditEstateActivity : BaseActivity<ActivityAddEditEstateBinding>(), KoinComponent, OnMapReadyCallback, ImagePicker.OnImageSelectedListener {

    private val addEditEstateViewModel: AddEditEstateViewModel by viewModel()

    private var mAdapter: ListImageWithDescriptionAdapter? = null

    private var map: GoogleMap? = null
    private var marker: Marker? = null
    var options = GoogleMapOptions().liteMode(true)

    lateinit var imagePicker: ImagePicker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_estate)

        binding = ActivityAddEditEstateBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setApiKey()
        setUpListenersAndObservers()
        initMapView(savedInstanceState)
        initRecycler()

        imagePicker = ImagePicker(this, BuildConfig.APPLICATION_ID)
        imagePicker.setImageSelectedListener(this)
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

        // Observe the list of ImageWithDescription
        addEditEstateViewModel.pictures.observe(this) { pictures ->
            refreshRecycler(pictures)
        }

        // Observe the used currency
        addEditEstateViewModel.isDollar.observe(this) { isDollar ->
            if(isDollar) binding?.addEditEstateCurrency?.text = "$"
            else binding?.addEditEstateCurrency?.text = "€"
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
    }

    private fun setApiKey() {
        if(!addEditEstateViewModel.isPositionStackApiKeyDefined()) addEditEstateViewModel.setPositionStackApiKey(getString(R.string.position_stack_api_key))
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        binding?.apply {
            addEditEstateLiteMap.onCreate(savedInstanceState)
            addEditEstateLiteMap.getMapAsync(this@AddEditEstateActivity)
        }
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
            if (options[item] == getString(R.string.add_edit_estate_save_take_picture_item)) imagePicker.takePhotoFromCamera()
            if (options[item] == getString(R.string.add_edit_estate_save_from_gallery_item)) imagePicker.takePhotoFromGallery()
        }
        builder.show()
    }

    override fun onImageSelectFailure() {
        Toast.makeText(this, "Error with image selection...", Toast.LENGTH_LONG).show()
    }

    override fun onImageSelectSuccess(imagePath: String) {
        addEditEstateViewModel.addPicture(imagePath)
    }

    private fun initRecycler() {
        mAdapter = ListImageWithDescriptionAdapter(ArrayList(), this)
        binding!!.addEditEstateListImages.apply {
            layoutManager = LinearLayoutManager(this@AddEditEstateActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
    }

    private fun refreshRecycler(myList: List<ImageWithDescription>) {
        mAdapter!!.updateList(myList)
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

}