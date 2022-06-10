package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.openclassrooms.realestatemanager.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.GlobalContext.startKoin

class RealEstateManagerApp: MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // For SDK < 20
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidLogger() // Use AndroidLogger as Koin Logger (default Level.INFO
            androidContext(this@RealEstateManagerApp) // Use Context From RealEstateManagerApp
            androidFileProperties() // Load properties from assets/koin.properties file
            fragmentFactory() // Setup a KoinFragmentFactory instance
            koin.loadModules(
                listOf(AppModule.applicationModule)
            )
        }
    }

}