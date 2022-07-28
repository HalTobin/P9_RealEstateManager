package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Agent(
    @PrimaryKey val id: Int? = null,
    var firstName: String? = "",
    var lastName: String? = ""
) {

    // Get the names of an agent in the following form "Firstname LASTNAME"
    fun getFullName(): String {
        return firstName.plus(" ").plus(lastName?.uppercase())
    }

    companion object {
        // Get an agent in a list by its full name
        fun getAgentByFullName(agents: List<Agent>, fullName: String): Agent? {
            var myAgent: Agent? = null
            agents.forEach {
                if (it.getFullName() == fullName) myAgent = it
            }
            return myAgent
        }

        // Get an agent in a list by its id
        fun getAgentById(agents: List<Agent>, id: Int): Agent? {
            var myAgent: Agent? = null
            agents.forEach {
                if (it.id == id) myAgent = it
            }
            return myAgent
        }
    }

}