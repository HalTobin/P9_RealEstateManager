package com.openclassrooms.realestatemanager.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.ui.fragment.DetailsFragment
import com.openclassrooms.realestatemanager.ui.fragment.ListFragment
import com.openclassrooms.realestatemanager.ui.fragment.MapFragment
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import kotlinx.android.synthetic.main.dialog_search_estate.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    var context: Context? = null

    private val mainViewModel: MainViewModel by viewModel()

    private val listFragment: ListFragment by inject()
    private val mapFragment: MapFragment by inject()
    private val detailsFragment: DetailsFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding!!.root)

        this.context = this

        if(isLarge()) {
            setUpClassicLayout()
        } else {
            setUpForLargeLayout()
        }
        setListeners()

        checkAndAskPermission()
        mainViewModel.findCurrentLocation(this)
    }

    override fun onBackPressed() {
        if(!isLarge()) {
            removeDetailsFragment()
            //setFragment(supportFragmentManager, R.id.main_fragment_map_details, mapFragment)
        }
        else { super.onBackPressed() }
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

    private fun setUpForLargeLayout() {
        setFragment(supportFragmentManager, R.id.main_fragment_list, listFragment)
        setFragment(supportFragmentManager, R.id.main_fragment_map_details, mapFragment)
        mainViewModel.selection.observe(this) { id ->
            mainViewModel.setEstateId(id)
            addDetailsFragment()
        }
    }

    private fun setListeners() {
        binding?.mainListBtAdd?.setOnClickListener { navigateToAddEditActivity(this) }

        binding?.mainAppbar?.mainSearch?.setOnClickListener { showSearchDialog() }

        mainViewModel.closeDetails.observe(this) {
            if(it) setFragment(supportFragmentManager, R.id.main_fragment_map_details, mapFragment)
        }
    }

    // Check permissions
    private fun checkAndAskPermission() {
        Dexter.withContext(context).withPermissions(
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
                //isPermissionGranted = false
                permissionToken.continuePermissionRequest()
            }
        }).check()
    }

    // Check if the user's device is large or not
    private fun isLarge(): Boolean {
        return (binding?.mainFrameLayout != null)
    }

    @SuppressLint("InflateParams")
    private fun showSearchDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_search_estate, null)

        with(builder) {
            setView(dialogLayout)
            show()

            val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this@MainActivity,
                R.layout.item_spinner_estate_type,
                Estate.getEstateTypes(this@MainActivity)
            )

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            dialogLayout.search_estate_type.setAdapter(spinnerAdapter)
            // When user select a List-Item.
            dialogLayout.search_estate_type?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mainViewModel.setSearch(position, Estate.TYPE)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            dialogLayout.search_estate_country.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.isNotEmpty()) mainViewModel.setSearch(s.toString(), Estate.COUNTRY)
                }
            })

            dialogLayout.search_estate_city.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.isNotEmpty()) mainViewModel.setSearch(s.toString(), Estate.CITY)
                }
            })

            dialogLayout.search_estate_zip.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.isNotEmpty()) mainViewModel.setSearch(s.toString(), Estate.ZIPCODE)
                }
            })

            dialogLayout.search_estate_area.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.isNotEmpty()) mainViewModel.setSearch(s.toString().toInt(), Estate.AREA)
                }
            })

            dialogLayout.search_estate_price.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.isNotEmpty()) mainViewModel.setSearch(s.toString().toInt(), Estate.PRICE)
                }
            })

            // Observe the used currency
            dialogLayout.search_estate_currency.setOnClickListener() {
                mainViewModel.invertCurrency()
            }

            // Observe the used currency
            mainViewModel.isSearchInDollar.observe(this@MainActivity) { isDollar ->
                if(isDollar) dialogLayout.search_estate_currency.text = "$"
                else dialogLayout.search_estate_currency.text = "€"
            }

            dialogLayout.search_estate_nbRooms.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.isNotEmpty()) mainViewModel.setSearch(s.toString().toInt(), Estate.ROOMS)
                }
            })

            dialogLayout.search_estate_nbBedrooms.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.isNotEmpty()) mainViewModel.setSearch(s.toString().toInt(), Estate.BEDROOMS)
                }
            })

            dialogLayout.search_estate_nbBathrooms.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.isNotEmpty()) mainViewModel.setSearch(s.toString().toInt(), Estate.BATHROOMS)
                }
            })

            // Checkbox listener to indicate if there is a park near the Estate
            dialogLayout.search_estate_check_park.setOnCheckedChangeListener { _, isChecked ->
                mainViewModel.setSearch(isChecked, Estate.PARK)
            }

            // Checkbox listener to indicate if there is a school near the Estate
            dialogLayout.search_estate_check_school.setOnCheckedChangeListener { _, isChecked ->
                mainViewModel.setSearch(isChecked, Estate.SCHOOL)
            }

            // Checkbox listener to indicate if there is a shop near the Estate
            dialogLayout.search_estate_check_shop.setOnCheckedChangeListener { _, isChecked ->
                mainViewModel.setSearch(isChecked, Estate.SHOP)
            }

            dialogLayout.search_estate_agent.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.isNotEmpty()) mainViewModel.setSearch(s.toString().toInt(), Estate.AGENT)
                }
            })

            dialogLayout.search_estate_search.setOnClickListener {
                mainViewModel.search()
            }
        }
    }

    // Setup fragment
    private fun addDetailsFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.main_fragment_map_details, detailsFragment)
        fragmentTransaction.commit()
    }

    // Setup fragment
    private fun removeDetailsFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(detailsFragment)
        //fragmentTransaction.remove(R.id.main_fragment_map_details, detailsFragment)
        fragmentTransaction.commit()
    }

    companion object {
        // Setup fragment
        fun setFragment(fragmentManager: FragmentManager, target: Int, fragment: Fragment,) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(target, fragment)
            fragmentTransaction.commit()
        }

        fun navigateToAddEditActivity(context: Context) {
            val intent = Intent(context, AddEditEstateActivity::class.java)
            ActivityCompat.startActivity(context, intent, null)
        }

        fun navigateToAddEditActivity(activity: FragmentActivity, estateId: Int) {
            val intent = Intent(activity, AddEditEstateActivity::class.java)
            intent.putExtra("estate_id", estateId)
            ActivityCompat.startActivity(activity, intent, null)
        }

        fun navigateToEstateDetailsActivity(activity: FragmentActivity, estateId: Int) {
            val intent = Intent(activity, EstateDetailsActivity::class.java)
            intent.putExtra("estate_id", estateId)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

}