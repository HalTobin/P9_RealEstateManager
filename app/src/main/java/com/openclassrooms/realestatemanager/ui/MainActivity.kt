package com.openclassrooms.realestatemanager.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.fragment.ListFragment
import com.openclassrooms.realestatemanager.viewModel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    var context: Context? = null

    private val mainViewModel: MainViewModel by viewModel<MainViewModel>()

    private val listFragment: ListFragment by inject<ListFragment>()

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        // Setup Koin Fragment Factory
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //this.context = this

        setFragment(listFragment)
    }

    // Setup fragment
    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment, fragment)
            .commit()
    }

    /*private fun showSnackBar(txt: String) {
        Snackbar.make(mBinding.getRoot(), txt, Snackbar.LENGTH_LONG).show()
    }*/
}