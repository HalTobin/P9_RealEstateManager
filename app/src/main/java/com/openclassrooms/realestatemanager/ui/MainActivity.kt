package com.openclassrooms.realestatemanager.ui

import android.Manifest
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
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

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        // Setup Koin Fragment Factory
        setupKoinFragmentFactory()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        this.context = this

        checkAndAskPermission()
        setUpTabAndNav()
        setApiKey()
        mainViewModel.findCurrentLocation(this)
    }

    // Allow navigation between fragments (List, Map)
    private fun setUpTabAndNav() {
        setFragment(listFragment)
        binding?.apply {
            mainBottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.main_hub_menu_list -> {
                        setFragment(listFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.main_hub_menu_map -> {
                        setFragment(mapFragment)
                        return@setOnItemSelectedListener true
                    }
                    else -> return@setOnItemSelectedListener false
                }
            }
        }
    }

    //
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

    // Setup fragment
    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun setApiKey() {
        if(!mainViewModel.isPositionStackApiKeyDefined()) mainViewModel.setPositionStackApiKey(getString(R.string.position_stack_api_key))
    }
}