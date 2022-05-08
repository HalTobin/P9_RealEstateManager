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
import com.openclassrooms.realestatemanager.ui.MainActivity
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

        setUpAdapter()
        setListenersAndObservers()

        return mBinding!!.root
    }

    private fun setListenersAndObservers() {

        // Setup listener to navigate to AddEditActivity
        mBinding?.apply {
            listBtAdd.setOnClickListener {
                this@ListFragment.activity?.let { it1 -> MainActivity.navigateToAddEditActivity(it1) }
            }
        }

        // Setup observer for list of estate
        mainViewModel.getEstates().observe(viewLifecycleOwner) { estates: List<Estate> ->
            refreshRecycler(estates)
        }
    }

    private fun setUpAdapter() {
        mAdapter = ListEstatePagerAdapter(ArrayList(), this.requireContext())
        mBinding!!.listEstateRecycler.layoutManager = LinearLayoutManager(this.context)
        mBinding!!.listEstateRecycler.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
        initRecycler()
    }

    private fun initRecycler() {
        mBinding!!.listEstateRecycler.adapter = mAdapter
    }

    private fun refreshRecycler(myList: List<Estate>) {
        mAdapter!!.updateList(myList)
    }

    override fun onResume() {
        super.onResume()
        initRecycler()
    }

}