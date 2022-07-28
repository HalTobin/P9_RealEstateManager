package com.openclassrooms.realestatemanager.util

import com.openclassrooms.realestatemanager.viewModel.AddEditViewModelTest
import java.util.*

object ApiPlaceholderLoader {

    fun getApiPlaceholder(): String {
        val inputStream = AddEditViewModelTest::class.java.getResourceAsStream("/CoordinatesApiPlaceholder.json")
        val s = Scanner(inputStream).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

}