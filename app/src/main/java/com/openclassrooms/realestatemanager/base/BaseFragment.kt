package com.openclassrooms.realestatemanager.base

import androidx.viewbinding.ViewBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.RealEstateManagerApp.Companion.modules
import com.openclassrooms.realestatemanager.di.ViewModelModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

// Use for binding of fragment
abstract class BaseFragment<T : ViewBinding?> : Fragment() {
    protected var binding: T? = null

    init{
        // Making sure we do not get "module already loaded" error
        //unloadKoinModules(modules)
        //loadKoinModules(modules)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val superclass = javaClass.genericSuperclass
        val aClass = (superclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        try {
            val method = aClass.getDeclaredMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.javaPrimitiveType
            )
            binding = method.invoke(null, layoutInflater, container, false) as T
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unloadKoinModules(modules)
    }
}