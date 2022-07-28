package com.openclassrooms.realestatemanager.ui.fragment

import com.openclassrooms.realestatemanager.base.BaseFragment
import com.openclassrooms.realestatemanager.ui.adapter.ListEstatePagerAdapter
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import coil.load
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding
import com.openclassrooms.realestatemanager.model.EstateUI
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.ArrayList

class ListFragment : BaseFragment<FragmentEstateListBinding?>(),
    ListEstatePagerAdapter.OnItemClick {

    private var mAdapter: ListEstatePagerAdapter? = null

    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstateListBinding.inflate(layoutInflater)

        setUpAdapter()
        setListenersAndObservers()

        return binding!!.root
    }

    private fun setListenersAndObservers() {
        mainViewModel.estates.observe(viewLifecycleOwner) { estates: List<EstateUI> ->
            mAdapter!!.updateList(estates)
            if (estates.isEmpty()) {
                binding?.listEstateNoEstateImage?.load(R.drawable.ic_estate)
                binding?.listEstateNoEstateText?.text = getString(R.string.estate_list_no_estate)
            } else {
                binding?.listEstateNoEstateImage?.load(0x00000000)
                binding?.listEstateNoEstateText?.text = " "
            }
        }
        mainViewModel.isInDollar.observe(viewLifecycleOwner) {
            mAdapter!!.updateCurrency(it)
        }
    }

    private fun setUpAdapter() {
        mAdapter = ListEstatePagerAdapter(ArrayList(), false, this.requireContext(), this)
        binding!!.listEstateRecycler.layoutManager = LinearLayoutManager(this.context)
        binding!!.listEstateRecycler.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
        initRecycler()
    }

    // Initialize the recycler that displays the list of estate
    private fun initRecycler() {
        binding!!.listEstateRecycler.adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
        initRecycler()
    }

    // Called when the user click on an estate of the list
    override fun onClick(estateId: Int) {
        mainViewModel.selectEstate(estateId)
    }

}