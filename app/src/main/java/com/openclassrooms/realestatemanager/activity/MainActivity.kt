package com.openclassrooms.realestatemanager.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    var context: Context? = null

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        this.context = this
    }

    // Setup fragment
    fun setFragment(fragment: Fragment?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_fragment, fragment!!)
        fragmentTransaction.commit()
    }

    /*private fun showSnackBar(txt: String) {
        Snackbar.make(mBinding.getRoot(), txt, Snackbar.LENGTH_LONG).show()
    }*/
}