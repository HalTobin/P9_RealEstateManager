package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.ui.fragment.DetailsFragment
import com.openclassrooms.realestatemanager.ui.fragment.ListFragment
import com.openclassrooms.realestatemanager.ui.fragment.MapFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.core.module.Module
import org.koin.dsl.module

// Module dedicated to the Fragments
object FragmentModule {

    val fragmentsModule: Module = module {

        fragment {
            provideListFragment()
        }

        fragment {
            provideMapFragment()
        }

        fragment {
            provideDetailsFragment()
        }

    }

    private fun provideListFragment(): ListFragment = ListFragment()

    private fun provideMapFragment(): MapFragment = MapFragment()

    private fun provideDetailsFragment(): DetailsFragment = DetailsFragment()

}