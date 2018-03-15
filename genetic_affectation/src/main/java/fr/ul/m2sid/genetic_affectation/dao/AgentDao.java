package fr.ul.m2sid.genetic_affectation.dao;


import fr.ul.m2sid.genetic_affectation.entity.Agent;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public interface AgentDao {

    TreeSet<Agent> getFreeAgents();
    Set<Agent> getCallCenters();
    ArrayList<Integer> getIdOTeleoperateurs();
    Agent getAgentById(Integer id);
}
