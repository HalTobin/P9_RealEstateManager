package com.openclassrooms.realestatemanager.activity.fragment

import com.openclassrooms.realestatemanager.base.BaseFragment
import com.openclassrooms.realestatemanager.activity.adapter.ListEstateAdapter
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import android.os.Bundle
import com.openclassrooms.realestatemanager.model.Estate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList


class ListFragment : BaseFragment<FragmentEstateListBinding?>() {
    private var mBinding: FragmentEstateListBinding? = null
    private var mAdapter: ListEstateAdapter? = null

    private val mainViewModel: MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.getEstates().observe(this) { estates: List<Estate> ->
            initRecycler()
            refreshRecycler(estates)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEstateListBinding.inflate(
            layoutInflater
        )
        mAdapter = ListEstateAdapter(ArrayList(), this.requireContext())
        mBinding!!.fragmentRestaurantList.layoutManager = LinearLayoutManager(this.context)
        mBinding!!.fragmentRestaurantList.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
        mBinding!!.fragmentRestaurantList.adapter = mAdapter
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

    companion object {
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }
}