package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Agent(@PrimaryKey val id: Int? = null,
                var firstName: String? = "",
                var lastName: String? = "") {

    fun getFullName(): String {
        return firstName.plus(" ").plus(lastName?.uppercase())
    }

    companion object {
        fun getAgentByFullName(agents: List<Agent>, fullName: String): Agent? {
            var myAgent: Agent? = null
            agents.forEach {
                if(it.getFullName() == fullName) myAgent = it
            }
            return myAgent
        }

        fun getAgentById(agents: List<Agent>, id: Int): Agent? {
            var myAgent: Agent? = null
            agents.forEach {
                if(it.id == id) myAgent = it
            }
            return myAgent
        }
    }

}