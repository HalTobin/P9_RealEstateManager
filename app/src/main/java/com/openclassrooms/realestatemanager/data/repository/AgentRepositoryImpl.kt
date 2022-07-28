package com.openclassrooms.realestatemanager.data.repository

import com.openclassrooms.realestatemanager.data.data_source.AgentDao
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.repository.AgentRepository
import kotlinx.coroutines.flow.Flow

class AgentRepositoryImpl(private val dao: AgentDao) : AgentRepository {

    override fun getAgents(): Flow<List<Agent>> {
        return dao.getAgents()
    }

    override fun getAgent(id: Int): Flow<Agent> {
        return dao.getAgentById(id)
    }

    override suspend fun addEstate(agent: Agent): Long {
        return dao.insertAgent(agent)
    }

}