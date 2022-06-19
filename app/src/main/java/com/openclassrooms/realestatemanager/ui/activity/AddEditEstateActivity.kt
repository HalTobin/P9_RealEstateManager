package com.openclassrooms.realestatemanager.ui.activity

import android.annotation.SuppressLint
import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
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
import com.openclassrooms.realestatemanager.model.Estate.Companion.getEstateTypes
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.ui.adapter.ListImageWithDescriptionAdapter
import com.openclassrooms.realestatemanager.util.EstateNotification
import com.openclassrooms.realestatemanager.util.MapUtils.getMapStyle
import com.openclassrooms.realestatemanager.util.MapUtils.navigateTo
import com.openclassrooms.realestatemanager.util.Utils.copyToInternal
import com.openclassrooms.realestatemanager.viewModel.AddEditEstateViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.net.URI


class AddEditEstateActivity : BaseActivity<ActivityAddEditEstateBinding>(),
    OnMapReadyCallback,
    ListImageWithDescriptionAdapter.OnItemClick {

    private val addEditEstateViewModel: AddEditEstateViewModel by viewModel()

    private var mAdapter: ListImageWithDescriptionAdapter? = null

    private var map: GoogleMap? = null
    private var marker: Marker? = null
    var options = GoogleMapOptions().liteMode(true)

    private var latestTmpUri: Uri? = null

    private val captureVideoResult = registerForActivityResult(ActivityResultContracts.CaptureVideo()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                showInputTextDialog(uri.copyToInternal(this).path)
            }
        }
    }

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                showInputTextDialog(uri.copyToInternal(this).path)
            }
        }
    }

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            showInputTextDialog(uri.copyToInternal(this).path)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)

        setUpListenersAndObservers()
        initMapView(savedInstanceState)
        initRecycler()
        initSpinner()

        val estateToLoadId = this.intent.getIntExtra("estate_id", -1)
        if(estateToLoadId != -1) addEditEstateViewModel.loadEstate(estateToLoadId)
    }

    override fun onBackPressed() {
        exitConfirmationDialog()
    }

    private fun setUpListenersAndObservers() {

        // Textfield listener for the Estate's title
        binding?.addEditEstateTitle?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setTitle(s.toString())
            }
        })

        // Textfield listener for the Estate's type
        binding?.addEditEstateTitle?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setTitle(s.toString())
            }
        })

        // Observer for the Estate's type
        addEditEstateViewModel.title.observe(this) {
            if(it != binding?.addEditEstateTitle?.text.toString()) binding?.addEditEstateTitle?.setText(it)
        }

        // TextField listener for the Estate's country
        binding?.addEditEstateCountry?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setCountry(s.toString())
            }
        })

        // Observer for the Estate's country
        addEditEstateViewModel.country.observe(this) {
            if(it != binding?.addEditEstateCountry?.text.toString()) binding?.addEditEstateCountry?.setText(it)
        }

        // TextField listener for the Estate's city
        binding?.addEditEstateCity?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setCity(s.toString())
            }
        })

        // Observer for the Estate's city
        addEditEstateViewModel.city.observe(this) {
            if(it != binding?.addEditEstateCity?.text.toString()) binding?.addEditEstateCity?.setText(it)
        }

        // TextField listener for the Estate's zip code
        binding?.addEditEstateZip?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setZip(s.toString())
            }
        })

        // Observer for the Estate's zip Code
        addEditEstateViewModel.zip.observe(this) {
            if(it != binding?.addEditEstateZip?.text.toString()) binding?.addEditEstateZip?.setText(it)
        }

        // TextField listener for the Estate's address
        binding?.addEditEstateAddress?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setAddress(s.toString())
            }
        })

        // Observer for the Estate's address
        addEditEstateViewModel.address.observe(this) {
            if(it != binding?.addEditEstateAddress?.text.toString()) binding?.addEditEstateAddress?.setText(it)
        }

        // TextField listener for the Estate's area
        binding?.addEditEstateArea?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setArea(s.toString())
            }
        })

        // Observer for the Estate's area
        addEditEstateViewModel.area.observe(this) {
            if(it.toString() != binding?.addEditEstateArea?.text.toString()) binding?.addEditEstateArea?.setText(it.toString())
        }

        // TextField listener for the Estate's price
        binding?.addEditEstatePrice?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setPrice(s.toString())
            }
        })

        // Observer for the Estate's price
        addEditEstateViewModel.price.observe(this) {
            if(it.toString() != binding?.addEditEstatePrice?.text.toString()) binding?.addEditEstatePrice?.setText(it.toString())
        }

        // TextField listener for the Estate's numbers of rooms
        binding?.addEditEstateNbRooms?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setRooms(s.toString())
            }
        })

        // Observer for the Estate's numbers of room
        addEditEstateViewModel.nbRooms.observe(this) {
            if(it.toString() != binding?.addEditEstateNbRooms?.text.toString()) binding?.addEditEstateNbRooms?.setText(it.toString())
        }

        // TextField listener for the Estate's numbers of bedrooms
        binding?.addEditEstateNbBedrooms?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setBedrooms(s.toString())
            }
        })

        // Observer for the Estate's numbers of bedrooms
        addEditEstateViewModel.nbBedrooms.observe(this) {
            if(it.toString() != binding?.addEditEstateNbBedrooms?.text.toString()) binding?.addEditEstateNbBedrooms?.setText(it.toString())
        }

        // TextField listener for the Estate's numbers of bathrooms
        binding?.addEditEstateNbBathrooms?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setBathrooms(s.toString())
            }
        })

        // Observer for the Estate's numbers of bathroom
        addEditEstateViewModel.nbBathrooms.observe(this) {
            if(it.toString() != binding?.addEditEstateNbBathrooms?.text.toString()) binding?.addEditEstateNbBathrooms?.setText(it.toString())
        }

        // Checkbox listener to indicate if there is a park near the Estate
        binding?.addEditEstateCheckPark?.setOnCheckedChangeListener { _, isChecked ->
            addEditEstateViewModel.setPark(isChecked)
        }

        // Observer for the Estate's park checkbox
        addEditEstateViewModel.nearbyPark.observe(this) {
            if(it != binding?.addEditEstateCheckPark?.isChecked) binding?.addEditEstateCheckPark?.isChecked = it
        }

        // Checkbox listener to indicate if there is a school near the Estate
        binding?.addEditEstateCheckSchool?.setOnCheckedChangeListener { _, isChecked ->
            addEditEstateViewModel.setSchool(isChecked)
        }

        // Observer for the Estate's school checkbox
        addEditEstateViewModel.nearbySchool.observe(this) {
            if(it != binding?.addEditEstateCheckSchool?.isChecked) binding?.addEditEstateCheckSchool?.isChecked = it
        }

        // Checkbox listener to indicate if there is a shop near the Estate
        binding?.addEditEstateCheckShop?.setOnCheckedChangeListener { _, isChecked ->
            addEditEstateViewModel.setShop(isChecked)
        }

        // Observer for the Estate's shop checkbox
        addEditEstateViewModel.nearbyShop.observe(this) {
            if(it != binding?.addEditEstateCheckShop?.isChecked) binding?.addEditEstateCheckShop?.isChecked = it
        }

        // TextField listener for the Estate's agent
        binding?.addEditEstateAgent?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setAgent(s.toString())
            }
        })

        // Observer for the Estate's agent
        addEditEstateViewModel.agent.observe(this) {
            if(it != binding?.addEditEstateAgent?.text.toString()) binding?.addEditEstateAgent?.setText(it)
        }

        // TextField listener for the Estate's description
        binding?.addEditEstateDescription?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty()) addEditEstateViewModel.setDescription(s.toString())
            }
        })

        // Observer for the Estate's numbers of bathroom
        addEditEstateViewModel.description.observe(this) {
            if(it != binding?.addEditEstateDescription?.text.toString()) binding?.addEditEstateDescription?.setText(it.toString())
        }

        // Click listener to find the Estate's location
        binding?.addEditEstateFind?.setOnClickListener {
            addEditEstateViewModel.searchLocation()
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

        // Click listener to change currency
        binding?.addEditEstateCurrency?.setOnClickListener {
            addEditEstateViewModel.changeCurrency()
        }

        // Observe the used currency
        addEditEstateViewModel.isDollar.observe(this) { isDollar ->
            if(isDollar) binding?.addEditEstateCurrency?.text = "$"
            else binding?.addEditEstateCurrency?.text = "€"
        }

        // Click listener to save the Estate
        binding?.addEditEstateSave?.setOnClickListener {
            addEditEstateViewModel.saveEstate()
        }

        // Click listener to add a picture
        binding?.addEditEstateAddImageCardView?.setOnClickListener {
            selectImage()
        }

        // Observe the list of ImageWithDescription
        addEditEstateViewModel.pictures.observe(this) { pictures ->
            refreshRecycler(pictures)
        }

        // Observe if a warning has been posted
        addEditEstateViewModel.warning.observe(this) { warning ->
            when(warning) {
                Estate.UNCOMPLETED -> showToast(R.string.add_edit_estate_uncomplete)
                Estate.CANT_FIND_LOCATION -> showToast(R.string.add_edit_estate_cant_find_location)
            }
        }

        // Observe if the activity must be closed
        addEditEstateViewModel.mustClose.observe(this) { mustClose ->
            EstateNotification.createNotification(this)
            if(mustClose) finish()
        }
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
    }

    // Ask the user if he's sure about closing the activity
    private fun exitConfirmationDialog() {
        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> { super.onBackPressed() }
                    DialogInterface.BUTTON_NEGATIVE -> {  }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.add_edit_estate_exit_text))
            .setPositiveButton(getString(R.string.add_edit_estate_exit_yes), dialogClickListener)
            .setNegativeButton(getString(R.string.add_edit_estate_exit_no), dialogClickListener)
            .show()
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>(getString(R.string.add_edit_estate_save_capture_video_item), getString(R.string.add_edit_estate_save_take_picture_item), getString(R.string.add_edit_estate_save_from_gallery_item))
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.add_edit_estate_save_choose_image_title))
        builder.setItems(options) { _, item ->
            if (options[item] == getString(R.string.add_edit_estate_save_capture_video_item)) captureVideo()
            if (options[item] == getString(R.string.add_edit_estate_save_take_picture_item)) takePicture()
            if (options[item] == getString(R.string.add_edit_estate_save_from_gallery_item)) selectImageFromGallery()
        }
        builder.show()
    }

    private fun captureVideo() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri(".mp4").let { uri ->
                latestTmpUri = uri
                captureVideoResult.launch(uri)
            }
        }
    }

    private fun takePicture() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri(".jpg").let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun getTmpFileUri(suffix: String): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", suffix, cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(applicationContext, "${BuildConfig.APPLICATION_ID}.fileprovider", tmpFile)
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

    private fun initSpinner() {
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.item_spinner_estate_type,
            getEstateTypes(this)
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding?.addEditEstateType?.setAdapter(spinnerAdapter)
        // When user select a List-Item.
        binding?.addEditEstateType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                addEditEstateViewModel.setType(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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

    override fun onImageClick(imageWithDescription: ImageWithDescription) {
        showDeleteImageDialog(imageWithDescription)
    }

    companion object {
        const val CAMERA_ACTION_CODE = 1
    }

}