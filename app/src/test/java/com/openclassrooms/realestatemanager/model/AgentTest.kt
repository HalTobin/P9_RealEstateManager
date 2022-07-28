package com.openclassrooms.realestatemanager.model

import com.google.common.truth.Truth
import com.openclassrooms.realestatemanager.model.Agent.Companion.getAgentByFullName
import com.openclassrooms.realestatemanager.model.Agent.Companion.getAgentById
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test

class AgentTest {

    private val dummyAgentList = listOf(
        Agent(id = 1, firstName = "Jim", lastName = "Hopper"),
        Agent(id = 2, firstName = "Dustin", lastName = "Henderson"),
        Agent(id = 3, firstName = "Joyce", lastName = "Byers"),
        Agent(id = 4, firstName = "Murray", lastName = "Bauman")
    )

    private val dummyAgent = Agent(id = 1, firstName = "Jim", lastName = "Hopper")

    @After
    fun tearDown() = clearAllMocks()

    @Test
    fun `check the complete name of an agent`() {
        // Given
        val agent = dummyAgent

        // When
        val fullName = agent.getFullName()

        // Then
        Truth.assertThat(fullName).isEqualTo("Jim HOPPER")
    }

    @Test
    fun `check the function that returns an agent by its complete name from a list of agents`() {
        // Given
        val agents = dummyAgentList
        val expectedAgent = dummyAgent

        // When
        val foundAgent = getAgentByFullName(agents, "Jim HOPPER")

        // Then
        Truth.assertThat(foundAgent).isEqualTo(expectedAgent)
    }

    @Test
    fun `check the function that returns an agent by its id from a list of agents`() {
        // Given
        val agents = dummyAgentList
        val expectedAgent = dummyAgent

        // When
        val foundAgent = getAgentById(agents, 1)

        // Then
        Truth.assertThat(foundAgent).isEqualTo(expectedAgent)
    }

}