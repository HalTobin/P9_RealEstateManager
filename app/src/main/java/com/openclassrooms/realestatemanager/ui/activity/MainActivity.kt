package com.openclassrooms.realestatemanager.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import coil.load
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.model.Estate.Companion.getEstateTypes
import com.openclassrooms.realestatemanager.model.EstateSearch
import com.openclassrooms.realestatemanager.ui.fragment.DetailsFragment
import com.openclassrooms.realestatemanager.ui.fragment.ListFragment
import com.openclassrooms.realestatemanager.ui.fragment.MapFragment
import com.openclassrooms.realestatemanager.util.Utils.fromDollarToEuro
import com.openclassrooms.realestatemanager.util.Utils.isAnAndroidTest
import com.openclassrooms.realestatemanager.util.Utils.isInternetAvailable
import com.openclassrooms.realestatemanager.util.Utils.toDateString
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import kotlinx.android.synthetic.main.dialog_search_estate.view.*
import kotlinx.android.synthetic.main.dialog_simple.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding>(), DatePickerDialog.OnDateSetListener {

    private val mainViewModel: MainViewModel by viewModel()

    private val listFragment: ListFragment by inject()
    private val mapFragment: MapFragment by inject()
    private val detailsFragment: DetailsFragment by inject()

    private var isInDollar = true

    private var isFragmentDetailsOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)

        // Loads the layout that correspond to the user's device
        if (isLarge()) {
            setUpForLargeLayout()
        } else {
            setUpClassicLayout()
        }
        setListeners()

        checkAndAskPermission()

        Log.i(
            "IS INTERNET AVAILABLE ", isInternetAvailable(
                this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ).toString()
        )

        mainViewModel.getImages().observe(this) { images ->
            if (!isAnAndroidTest()) mainViewModel.cleanImageFolder(this, images)
        }

        mainViewModel.findCurrentLocation(this)
    }

    // If the app runs on a large device,
    override fun onBackPressed() {
        if (isLarge()) {
            if (isFragmentDetailsOpen) removeDetailsFragment()
            else super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    // Allow navigation between fragments (List, Map)
    private fun setUpClassicLayout() {
        setFragment(supportFragmentManager, R.id.main_frame_layout, listFragment)
        binding?.apply {
            mainBottomNav?.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.main_hub_menu_list -> {
                        setFragment(supportFragmentManager, R.id.main_frame_layout, listFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.main_hub_menu_map -> {
                        setFragment(supportFragmentManager, R.id.main_frame_layout, mapFragment)
                        return@setOnItemSelectedListener true
                    }
                    else -> return@setOnItemSelectedListener false
                }
            }
        }
        mainViewModel.selection.observe(this) { id ->
            navigateToEstateDetailsActivity(this, id)
        }
    }

    // Load the fragments and the observer that correspond to the large layout
    private fun setUpForLargeLayout() {
        setFragment(supportFragmentManager, R.id.main_fragment_list, listFragment)
        setFragment(supportFragmentManager, R.id.main_fragment_map_details, mapFragment)
        mainViewModel.selection.observe(this) { id ->
            mainViewModel.setEstateId(id)
            if(!isFragmentDetailsOpen) addDetailsFragment()
        }
    }

    private fun setListeners() {
        binding?.apply {
            mainListBtAdd.setOnClickListener { navigateToAddEditActivity(this@MainActivity) }
            mainAppbar.mainSearch.setOnClickListener { showSearchDialog() }
            mainAppbar.mainChangeCurrency.setOnClickListener { mainViewModel.invertCurrency() }
        }

        mainViewModel.isInDollar.observe(this) {
            if (it) binding?.mainAppbar?.mainChangeCurrency?.load(R.drawable.ic_currency_dollar)
            else binding?.mainAppbar?.mainChangeCurrency?.load(R.drawable.ic_currency_euro)
            isInDollar = it
        }
    }

    // Check permissions
    private fun checkAndAskPermission() {
        Dexter.withContext(this).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                println("Permission granted")
            }

            override fun onPermissionRationaleShouldBeShown(
                list: List<PermissionRequest>,
                permissionToken: PermissionToken
            ) {
                permissionToken.continuePermissionRequest()
            }
        }).check()
    }

    // Check if the user's device is large or not
    private fun isLarge(): Boolean {
        return binding?.mainFrameLayout == null
    }

    @SuppressLint("InflateParams")
    private fun showSearchDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_search_estate, null)

        with(builder) {
            setView(dialogLayout)
            val ad = show()

            // Initialize the dropdown menu for estate's type
            val typeSpinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this@MainActivity,
                R.layout.item_spinner,
                getEstateTypes(this@MainActivity)
            )
            typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dialogLayout.search_estate_type.setAdapter(typeSpinnerAdapter)

            mainViewModel.getListOfAgent().observe(this@MainActivity) { agents ->

                // Initialize the dropdown menu for agents
                val agentsFullName = mutableListOf<String>()
                agents.forEach { agentsFullName.add(it.getFullName()) }
                val agentSpinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@MainActivity,
                    R.layout.item_spinner,
                    agentsFullName
                )
                agentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dialogLayout.search_estate_agent.setAdapter(agentSpinnerAdapter)

                mainViewModel.searchEstateLiveData.observe(this@MainActivity) { mySearchEstate ->
                    if (mySearchEstate.type != null)
                        dialogLayout.search_estate_type.setText(
                            getEstateTypes(this@MainActivity)[mySearchEstate.type!!],
                            false
                        )
                    dialogLayout.search_estate_city.setText(mySearchEstate.city)
                    dialogLayout.search_estate_zip.setText(mySearchEstate.zipCode)
                    dialogLayout.search_estate_country.setText(mySearchEstate.country)
                    if (mySearchEstate.priceMinDollar != null)
                        if (isInDollar)
                            dialogLayout.search_estate_min_price.setText(mySearchEstate.priceMinDollar!!.toString())
                        else
                            dialogLayout.search_estate_min_price.setText(
                                mySearchEstate.priceMinDollar!!.fromDollarToEuro().toString()
                            )
                    if (mySearchEstate.priceMaxDollar != null)
                        if (isInDollar)
                            dialogLayout.search_estate_max_price.setText(mySearchEstate.priceMaxDollar!!.toString())
                        else
                            dialogLayout.search_estate_max_price.setText(
                                mySearchEstate.priceMaxDollar!!.fromDollarToEuro().toString()
                            )
                    if (mySearchEstate.areaMin != null)
                        dialogLayout.search_estate_min_area.setText(mySearchEstate.areaMin!!.toString())
                    if (mySearchEstate.areaMax != null)
                        dialogLayout.search_estate_max_area.setText(mySearchEstate.areaMax!!.toString())
                    dialogLayout.search_estate_check_park.isChecked = mySearchEstate.nearbyPark!!
                    dialogLayout.search_estate_check_school.isChecked =
                        mySearchEstate.nearbySchool!!
                    dialogLayout.search_estate_check_shop.isChecked = mySearchEstate.nearbyShop!!
                    dialogLayout.search_estate_check_sold.isChecked = mySearchEstate.sold!!
                    if (!mySearchEstate.sold!!) {
                        dialogLayout.search_estate_sold_since_card.isClickable = false
                        dialogLayout.search_estate_sold_since_card.isVisible = false
                    } else {
                        dialogLayout.search_estate_sold_since_card.isClickable = true
                        dialogLayout.search_estate_sold_since_card.isVisible = true
                    }
                    if (mySearchEstate.agentId != null)
                        dialogLayout.search_estate_agent.setText(
                            Agent.getAgentById(
                                agents,
                                mySearchEstate.agentId!!
                            )?.getFullName(),
                            false
                        )
                    if (mySearchEstate.nbRooms != null)
                        dialogLayout.search_estate_nbRooms.setText(mySearchEstate.nbRooms!!.toString())
                    if (mySearchEstate.nbBedrooms != null)
                        dialogLayout.search_estate_nbBedrooms.setText(mySearchEstate.nbBedrooms!!.toString())
                    if (mySearchEstate.nbBathrooms != null)
                        dialogLayout.search_estate_nbBathrooms.setText(mySearchEstate.nbBathrooms!!.toString())
                    if (mySearchEstate.nbImages != null)
                        dialogLayout.search_estate_pictures.setText(mySearchEstate.nbImages!!.toString())
                    if (mySearchEstate.entryDate != null)
                        dialogLayout.search_estate_date_in_sale_since_text.text =
                            mySearchEstate.entryDate!!.toDateString()
                    else dialogLayout.search_estate_date_in_sale_since_text.text =
                        getString(R.string.hint_date)
                    if (mySearchEstate.soldDate != null)
                        dialogLayout.search_estate_date_sold_since_text.text =
                            mySearchEstate.soldDate!!.toDateString()
                    else dialogLayout.search_estate_date_sold_since_text.text =
                        getString(R.string.hint_date)

                    Log.i("SEARCH ESTATE", "Search parameters has been modified")
                }

                // TextInput listener to find an Estate by its agent
                dialogLayout.search_estate_agent.doAfterTextChanged { text ->
                    text?.let {
                        if (it.isNotEmpty()) mainViewModel.setSearch(
                            Agent.getAgentByFullName(agents, text.toString())!!.id!!,
                            EstateSearch.AGENT
                        ) else mainViewModel.setSearchNull(EstateSearch.AGENT)
                    }
                }

            }

            mainViewModel.forceRefreshSearchEstate()

            // When user select an estate Type
            dialogLayout.search_estate_type.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        getEstateTypes(this@MainActivity).indexOf(it.toString()),
                        EstateSearch.TYPE
                    ) else mainViewModel.setSearchNull(EstateSearch.TYPE)
                }
            }

            // TextInput listener to find an Estate by its country
            dialogLayout.search_estate_country.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        it.toString(),
                        EstateSearch.COUNTRY
                    ) else mainViewModel.setSearchNull(EstateSearch.COUNTRY)
                }
            }

            // TextInput listener to find an Estate by its city
            dialogLayout.search_estate_city.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(it.toString(), EstateSearch.CITY)
                    else mainViewModel.setSearchNull(EstateSearch.CITY)
                }
            }

            // TextInput listener to find an Estate by its zip code
            dialogLayout.search_estate_zip.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        it.toString(),
                        EstateSearch.ZIPCODE
                    )
                    else mainViewModel.setSearchNull(EstateSearch.ZIPCODE)
                }
            }

            // TextInput listener to find an Estate with a minimum area of the input value
            dialogLayout.search_estate_min_area.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        it.toString().toInt(),
                        EstateSearch.AREA_MIN
                    )
                    else mainViewModel.setSearchNull(EstateSearch.AREA_MIN)
                }
            }

            // TextInput listener to find an Estate with a maximum area of the input value
            dialogLayout.search_estate_max_area.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        it.toString().toInt(),
                        EstateSearch.AREA_MAX
                    )
                    else mainViewModel.setSearchNull(EstateSearch.AREA_MAX)
                }
            }

            // TextInput listener to find an Estate that cost more than the defined minimum
            dialogLayout.search_estate_min_price.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        it.toString().toInt(),
                        EstateSearch.PRICE_MIN
                    ) else mainViewModel.setSearchNull(EstateSearch.PRICE_MIN)
                }
            }

            // TextInput listener to find an Estate that cost less than the defined maximum
            dialogLayout.search_estate_max_price.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        it.toString().toInt(),
                        EstateSearch.PRICE_MAX
                    ) else mainViewModel.setSearchNull(EstateSearch.PRICE_MAX)
                }
            }

            // TextInput listener to find an Estate by its number of rooms
            dialogLayout.search_estate_nbRooms.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        it.toString().toInt(),
                        EstateSearch.ROOMS
                    ) else mainViewModel.setSearchNull(EstateSearch.ROOMS)
                }
            }

            // TextInput listener to find an Estate by its number of bedrooms
            dialogLayout.search_estate_nbBedrooms.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        it.toString().toInt(),
                        EstateSearch.BEDROOMS
                    ) else mainViewModel.setSearchNull(EstateSearch.BEDROOMS)
                }
            }

            // TextInput listener to find an Estate by its number of bathrooms
            dialogLayout.search_estate_nbBathrooms.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        it.toString().toInt(),
                        EstateSearch.BATHROOMS
                    ) else mainViewModel.setSearchNull(EstateSearch.BATHROOMS)
                }
            }

            // Checkbox listener to find an Estate close to a park
            dialogLayout.search_estate_check_park.setOnCheckedChangeListener { _, isChecked ->
                mainViewModel.setSearch(isChecked, EstateSearch.PARK)
            }

            // Checkbox listener to find an Estate close to a school
            dialogLayout.search_estate_check_school.setOnCheckedChangeListener { _, isChecked ->
                mainViewModel.setSearch(isChecked, EstateSearch.SCHOOL)
            }

            // Checkbox listener to find an Estate close to a shop
            dialogLayout.search_estate_check_shop.setOnCheckedChangeListener { _, isChecked ->
                mainViewModel.setSearch(isChecked, EstateSearch.SHOP)
            }

            // Checkbox listener to find a sold Estate
            dialogLayout.search_estate_check_sold.setOnCheckedChangeListener { _, isChecked ->
                mainViewModel.setSearch(isChecked, EstateSearch.SOLD)
                if (!isChecked) mainViewModel.setSearchNull(EstateSearch.SOLD_SINCE)
            }

            dialogLayout.search_estate_pictures.doAfterTextChanged { text ->
                text?.let {
                    if (it.isNotEmpty()) mainViewModel.setSearch(
                        text.toString().toInt(),
                        EstateSearch.IMAGES
                    ) else mainViewModel.setSearchNull(EstateSearch.IMAGES)
                }
            }

            dialogLayout.search_estate_date_in_sale_since_card.setOnClickListener {
                val datePickerDialog = DatePickerDialog(
                    this@MainActivity,
                    this@MainActivity,
                    Calendar.getInstance()[Calendar.YEAR],
                    Calendar.getInstance()[Calendar.MONTH],
                    Calendar.getInstance()[Calendar.DAY_OF_MONTH]
                )
                datePickerDialog.show()
                datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal[Calendar.YEAR] = year
                    cal[Calendar.MONTH] = month
                    cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                    mainViewModel.setSearch(cal.timeInMillis, EstateSearch.IN_SALE_SINCE)
                }
            }

            dialogLayout.search_estate_date_in_sale_since_card.setOnLongClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                val dialogLayout = layoutInflater.inflate(R.layout.dialog_simple, null)
                with(builder) {
                    dialogLayout.dialog_simple_content.text =
                        getString(R.string.dialog_search_reset_date)
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    setView(dialogLayout)
                    setTitle(getString(R.string.reset))
                    // Set up the buttons
                    setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                        // Here you get get input text from the Edittext
                        mainViewModel.setSearchNull(EstateSearch.IN_SALE_SINCE)
                        dialog.cancel()
                    }
                    setNegativeButton(getString(R.string.no)) { dialog, _ ->
                        dialog.cancel()
                    }
                    show()
                }
                return@setOnLongClickListener true
            }

            dialogLayout.search_estate_sold_since_card.setOnClickListener {
                val datePickerDialog = DatePickerDialog(
                    this@MainActivity,
                    this@MainActivity,
                    Calendar.getInstance()[Calendar.YEAR],
                    Calendar.getInstance()[Calendar.MONTH],
                    Calendar.getInstance()[Calendar.DAY_OF_MONTH]
                )
                datePickerDialog.show()
                datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    cal[Calendar.YEAR] = year
                    cal[Calendar.MONTH] = month
                    cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                    mainViewModel.setSearch(cal.timeInMillis, EstateSearch.SOLD_SINCE)
                }
            }

            // Button's listener to reset the search
            dialogLayout.search_estate_reset.setOnClickListener {
                mainViewModel.stopSearch()
                ad.dismiss()
            }

            // Button's listener to start a search
            dialogLayout.search_estate_search.setOnClickListener {
                mainViewModel.startSearch()
                ad.dismiss()
            }
        }
    }

    // Setup fragment
    private fun addDetailsFragment() {
        isFragmentDetailsOpen = true
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.main_fragment_map_details, detailsFragment)
        fragmentTransaction.commit()
    }

    // Setup fragment
    private fun removeDetailsFragment() {
        isFragmentDetailsOpen = false
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(detailsFragment)
        fragmentTransaction.commit()
    }

    companion object {
        // Setup fragment
        fun setFragment(fragmentManager: FragmentManager, target: Int, fragment: Fragment) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(target, fragment)
            fragmentTransaction.commit()
        }

        // Open the AddEditActivity
        fun navigateToAddEditActivity(context: Context) {
            val intent = Intent(context, AddEditEstateActivity::class.java)
            ActivityCompat.startActivity(context, intent, null)
        }

        // Open the AddEditActivity with a given id
        fun navigateToAddEditActivity(activity: FragmentActivity, estateId: Int) {
            val intent = Intent(activity, AddEditEstateActivity::class.java)
            intent.putExtra("estate_id", estateId)
            ActivityCompat.startActivity(activity, intent, null)
        }

        // Open the EstateDetailsActivity with a given id
        fun navigateToEstateDetailsActivity(activity: FragmentActivity, estateId: Int) {
            val intent = Intent(activity, EstateDetailsActivity::class.java)
            intent.putExtra("estate_id", estateId)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Log.i("DATE SELECTION", "A date has been selected")
    }

}