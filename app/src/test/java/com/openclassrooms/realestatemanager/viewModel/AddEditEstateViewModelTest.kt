package com.openclassrooms.realestatemanager.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.repository.CoordinatesRepository
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.ImageRepository
import com.openclassrooms.realestatemanager.util.MainCoroutineRule
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

class AddEditEstateViewModelTest {

    // Subject under test
    private lateinit var addEditEstateViewModel: AddEditEstateViewModel

    // Use a fake UseCase to be injected into the viewModel

    private val coordinatesRepository: CoordinatesRepository = mockk()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @After
    fun tearDown() = clearAllMocks()


}