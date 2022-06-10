package com.openclassrooms.realestatemanager.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.fragment.DetailsFragment
import com.openclassrooms.realestatemanager.ui.fragment.ListFragment
import com.openclassrooms.realestatemanager.ui.fragment.MapFragment
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
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
        // Setup Koin Fragment Factory
        setupKoinFragmentFactory()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
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
        if(!isLarge()) { setFragment(supportFragmentManager, R.id.main_fragment_map_details, mapFragment) }
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
            setFragment(supportFragmentManager, R.id.main_fragment_map_details, detailsFragment)
        }
    }

    private fun setListeners() {
        binding?.mainListBtAdd?.setOnClickListener { navigateToAddEditActivity(this) }
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