package com.openclassrooms.realestatemanager.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityAddEditEstateBinding
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.ui.adapter.ListImageWithDescriptionAdapter
import com.openclassrooms.realestatemanager.util.MapUtils.getMapStyle
import com.openclassrooms.realestatemanager.util.MapUtils.navigateTo
import com.openclassrooms.realestatemanager.viewModel.AddEditEstateViewModel
import lib.android.imagepicker.ImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent


class AddEditEstateActivity : BaseActivity<ActivityAddEditEstateBinding>(), KoinComponent, OnMapReadyCallback, ImagePicker.OnImageSelectedListener, ListImageWithDescriptionAdapter.OnItemClick {

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

        // Textfield for the Estate's title
        binding?.addEditEstateTitle?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setTitle(s.toString())
            }
        })

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

        // TextField for the Estate's area
        binding?.addEditEstateArea?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setArea(s.toString())
            }
        })

        // TextField for the Estate's price
        binding?.addEditEstatePrice?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setPrice(s.toString())
            }
        })

        // TextField for the Estate's numbers of rooms
        binding?.addEditEstateNbRooms?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setRooms(s.toString())
            }
        })

        // TextField for the Estate's numbers of bedrooms
        binding?.addEditEstateNbBedrooms?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setBedrooms(s.toString())
            }
        })

        // TextField for the Estate's numbers of bathrooms
        binding?.addEditEstateNbBathrooms?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setBathrooms(s.toString())
            }
        })

        // Checkbox to indicate if there is a park near the Estate
        binding?.addEditEstateCheckPark?.setOnCheckedChangeListener { _, isChecked ->
            addEditEstateViewModel.setPark(isChecked)
        }

        // Checkbox to indicate if there is a school near the Estate
        binding?.addEditEstateCheckSchool?.setOnCheckedChangeListener { _, isChecked ->
            addEditEstateViewModel.setSchool(isChecked)
        }

        // Checkbox to indicate if there is a shop near the Estate
        binding?.addEditEstateCheckShop?.setOnCheckedChangeListener { _, isChecked ->
            addEditEstateViewModel.setShop(isChecked)
        }

        // TextField for the Estate's agent
        binding?.addEditEstateAgent?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) addEditEstateViewModel.setAgent(s.toString())
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

        // Click listener to save the Estate
        binding?.addEditEstateSave?.setOnClickListener {
            addEditEstateViewModel.saveEstate()
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

        // Observe if a warning has been posted
        addEditEstateViewModel.warning.observe(this) { warning ->
            when(warning) {
                Estate.UNCOMPLETE -> showToast(R.string.add_edit_estate_uncomplete)
                Estate.CANT_FIND_LOCATION -> showToast(R.string.add_edit_estate_cant_find_location)
            }
        }

        // Observe if the activity must be closed
        addEditEstateViewModel.mustClose.observe(this) { mustClose ->
            if(mustClose) finish()
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
        builder.setItems(options) { dialog, item ->
            if (options[item] == getString(R.string.add_edit_estate_save_take_picture_item)) imagePicker.takePhotoFromCamera()
            if (options[item] == getString(R.string.add_edit_estate_save_from_gallery_item)) imagePicker.takePhotoFromGallery()
        }
        builder.show()
    }

    override fun onImageSelectFailure() {
        Toast.makeText(this, "Error with image selection...", Toast.LENGTH_LONG).show()
    }

    override fun onImageSelectSuccess(imagePath: String) {
        showInputTextDialog(imagePath)
    }

    private fun initRecycler() {
        mAdapter = ListImageWithDescriptionAdapter(ArrayList(), this, this)
        binding!!.addEditEstateListImages.apply {
            layoutManager = LinearLayoutManager(this@AddEditEstateActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
    }

    private fun refreshRecycler(myList: List<ImageWithDescription>) {
        mAdapter!!.updateList(myList)
    }

    private fun showToast(contentId: Int) {
        Toast.makeText(this, contentId, Toast.LENGTH_SHORT).show()
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

    @SuppressLint("InflateParams")
    private fun showInputTextDialog(imagePath: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_text_input, null)
        val input = dialogLayout.findViewById<TextInputEditText>(R.id.add_edit_estate_add_image_dialog_text)

        with(builder) {
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            setView(dialogLayout)

            // Set up the buttons
            setPositiveButton(getString(R.string.ok_button)) { dialog, _ ->
                // Here you get get input text from the Edittext
                addEditEstateViewModel.addPicture(imagePath, input.text.toString())
                dialog.cancel()
            }
            show()
        }
    }

    @SuppressLint("InflateParams")
    private fun showDeleteImageDialog(imageWithDescription: ImageWithDescription){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_delete_confirmation, null)

        with(builder) {
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            setView(dialogLayout)

            setTitle(getString(R.string.are_you_sure))

            // Set up the buttons
            setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                // Here you get get input text from the Edittext
                addEditEstateViewModel.removePicture(imageWithDescription)
                dialog.cancel()
            }
            setNegativeButton(getString(R.string.cancel_button)) { dialog, _ ->
                dialog.cancel()
            }
            show()
        }
    }

    override fun onClick(imageWithDescription: ImageWithDescription) {
        showDeleteImageDialog(imageWithDescription)
    }

}