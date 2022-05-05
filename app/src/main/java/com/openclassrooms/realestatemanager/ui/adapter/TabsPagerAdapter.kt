package com.openclassrooms.realestatemanager.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager, lifecycle) {

    private val myFragments: MutableList<Fragment> = ArrayList()

    fun addFragment(fragment: Fragment) {
        myFragments.add(fragment)
    }

    /**
     * get the number of pages
     * @return
     */
    override fun getItemCount(): Int {
        return myFragments.size
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * @param position
     * @return
     */
    override fun createFragment(position: Int): Fragment {
        return myFragments[position]
    }

}