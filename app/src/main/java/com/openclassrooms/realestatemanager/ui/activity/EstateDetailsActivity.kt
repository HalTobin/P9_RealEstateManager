package com.openclassrooms.realestatemanager.ui.activity

import android.os.Bundle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityEstateDetailsBinding
import com.openclassrooms.realestatemanager.ui.activity.MainActivity.Companion.setFragment
import com.openclassrooms.realestatemanager.ui.fragment.DetailsFragment
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateDetailsActivity : BaseActivity<ActivityEstateDetailsBinding>() {

    private val mainViewModel: MainViewModel by viewModel()

    private val detailsFragment: DetailsFragment by inject()

    private var estateId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstateDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        estateId = intent.getIntExtra("estate_id", -1)
        mainViewModel.setEstateId(estateId)

        setFragment(supportFragmentManager, R.id.fragment_estate_details, detailsFragment)
    }

}