package com.openclassrooms.realestatemanager.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.adapter.TabsPagerAdapter
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

        initTab()
    }

    private fun initTab() {
        val tabsPagerAdapter = TabsPagerAdapter(supportFragmentManager, lifecycle)
        tabsPagerAdapter.addFragment(listFragment)
        tabsPagerAdapter.addFragment(mapFragment)

        val viewPager: ViewPager2 = binding!!.mainViewPager

        viewPager.adapter = tabsPagerAdapter
        val tabs: TabLayout = binding!!.mainTabs
        TabLayoutMediator(tabs, viewPager) { tabs, position ->
            when(position) {
                0 -> tabs.text = context?.getString(R.string.main_tab_estate_list)
                1 -> tabs.text = context?.getString(R.string.main_tab_estate_map)
            }
        }.attach()

        //setFragment(listFragment)

        /*binding.mainTabs = ListEstatePagerAdapter(supportFragmentManager)

        mPagerAdapter.addFragment(listFragment)
        mPagerAdapter.addFragment(FavoriteFragment.newInstance())
        mViewPager.setAdapter(mPagerAdapter)

        mViewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(mTabLayout))
        mTabLayout.addOnTabSelectedListener(ViewPagerOnTabSelectedListener(mViewPager))*/
    }

    // Setup fragment
    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_view_pager, fragment)
            .commit()
    }

    /*private fun showSnackBar(txt: String) {
        Snackbar.make(mBinding.getRoot(), txt, Snackbar.LENGTH_LONG).show()
    }*/
}