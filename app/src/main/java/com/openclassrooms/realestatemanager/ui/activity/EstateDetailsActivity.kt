package com.openclassrooms.realestatemanager.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityEstateDetailsBinding
import com.openclassrooms.realestatemanager.model.ImageWithDescription
import com.openclassrooms.realestatemanager.ui.adapter.ListImageWithDescriptionAdapter
import com.openclassrooms.realestatemanager.viewModel.EstateDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class EstateDetailsActivity : BaseActivity<ActivityEstateDetailsBinding>(), KoinComponent, OnMapReadyCallback, ListImageWithDescriptionAdapter.OnItemClick {

    private val estateDetailsViewModel: EstateDetailsViewModel by viewModel()

    private var mAdapter: ListImageWithDescriptionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstateDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setUpListenersAndObservers()
        initRecycler()
    }

    private fun initRecycler() {
        mAdapter = ListImageWithDescriptionAdapter(ArrayList(), this, this)
        binding!!.estateDetailsImageList.apply {
            layoutManager = LinearLayoutManager(this@EstateDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
    }

    private fun setUpListenersAndObservers() {
        //TODO - Observer for the Estate's from the ViewModel
        //TODO - Edit button set on click listener
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

    override fun onClick(imageWithDescription: ImageWithDescription) {
        //TODO - Display the selected image in fullscreen
        TODO("Not yet implemented")
    }

}