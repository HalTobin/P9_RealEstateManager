package com.openclassrooms.realestatemanager.ui.fragment

import com.openclassrooms.realestatemanager.base.BaseFragment
import com.openclassrooms.realestatemanager.ui.adapter.ListEstatePagerAdapter
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import android.os.Bundle
import com.openclassrooms.realestatemanager.model.Estate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding
import kotlinx.coroutines.Job
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent
import java.util.ArrayList

class ListFragment : BaseFragment<FragmentEstateListBinding?>(), KoinComponent {
    private var mBinding: FragmentEstateListBinding? = null
    private var mAdapter: ListEstatePagerAdapter? = null

    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEstateListBinding.inflate(
            layoutInflater
        )
        mAdapter = ListEstatePagerAdapter(ArrayList(), this.requireContext())
        mBinding!!.fragmentRestaurantList.layoutManager = LinearLayoutManager(this.context)
        mBinding!!.fragmentRestaurantList.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )

        initRecycler()

        mainViewModel.getEstates().observe(viewLifecycleOwner) { estates: List<Estate> ->
            refreshRecycler(estates)
        }

        return mBinding!!.root
    }

    private fun initRecycler() {
        mBinding!!.fragmentRestaurantList.adapter = mAdapter
    }

    private fun refreshRecycler(myList: List<Estate>) {
        mAdapter!!.updateList(myList)
    }

    override fun onResume() {
        super.onResume()
        initRecycler()
    }

}