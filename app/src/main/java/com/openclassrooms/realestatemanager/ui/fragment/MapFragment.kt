package com.openclassrooms.realestatemanager.ui.fragment

import com.openclassrooms.realestatemanager.base.BaseFragment
import com.openclassrooms.realestatemanager.ui.adapter.ListEstatePagerAdapter
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import android.os.Bundle
import com.openclassrooms.realestatemanager.model.Estate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.databinding.FragmentEstateMapBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent


class MapFragment : BaseFragment<FragmentEstateMapBinding?>(), KoinComponent {
    private var mBinding: FragmentEstateMapBinding? = null

    private val mainViewModel: MainViewModel by sharedViewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.getEstates().observe(this) { estates: List<Estate> ->
            //TODO
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEstateMapBinding.inflate(
            layoutInflater
        )

        return mBinding!!.root
    }

    override fun onResume() {
        super.onResume()
        //mBinding!!.mapMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //mBinding!!.mapMapView.onPause()
    }

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
}