package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.openclassrooms.realestatemanager.di.DataModule
import com.openclassrooms.realestatemanager.di.FakeDataModule
import com.openclassrooms.realestatemanager.di.FragmentModule
import com.openclassrooms.realestatemanager.di.ViewModelModule
import com.openclassrooms.realestatemanager.util.Utils.isAnAndroidTest
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RealEstateManagerApp : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // For SDK < 20
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger() // Use AndroidLogger as Koin Logger (default Level.INFO
            androidContext(this@RealEstateManagerApp) // Use Context From RealEstateManagerApp
            androidFileProperties() // Load properties from assets/koin.properties file

            if (isAnAndroidTest()) {
                koin.loadModules(androidTestModules)
            } else {
                koin.loadModules(modules)
            }

        }
    }

    companion object {
        val modules = listOf(
            ViewModelModule.viewModelModule,
            DataModule.dataModule,
            FragmentModule.fragmentsModule
        )
        val androidTestModules = listOf(
            ViewModelModule.viewModelModule,
            FakeDataModule.fakeDataModule,
            FragmentModule.fragmentsModule
        )
    }

}