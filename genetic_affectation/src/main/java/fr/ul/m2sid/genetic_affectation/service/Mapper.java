package fr.ul.m2sid.genetic_affectation.service;

import fr.ul.m2sid.genetic_affectation.dao.AgentDao;
import fr.ul.m2sid.genetic_affectation.entity.Agent;
import fr.ul.m2sid.genetic_affectation.entity.Event;
import fr.ul.m2sid.genetic_affectation.geneticAffectation.GeneticAffectation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.*;

@Service
public class Mapper {

    @Autowired
    private AgentDao agentDao;

    @Autowired
    private GeneticAffectation geneticAffectation;

    public Map<String, Integer> mapEventstoCallCenters(List<Event> eventsToAffect, Set<Agent> callCenters) {
        Map<String, Integer> solution = new HashMap<>();
        Integer idRandomCallCenter = callCenters.iterator().next().getId();
        for (Event event: eventsToAffect) {
            solution.put(event.getIdentifiant()+event.getEmplacement(),idRandomCallCenter);
        }
        return solution;
    }

    public Map<String, Integer> affectEventsGeneticMethod(@RequestBody TreeSet<Event> sortedEvents) throws IOException {

        ArrayList<Agent> agents = new ArrayList<Agent>(agentDao.getFreeAgents());
        System.out.println(agents);
        Set<Map<Event, Agent>> bestSolutions;
        Map<String, Integer> bestSolutionsInteger= new HashMap<>();
        Set<Agent> callCenters;

        if(agents.size()<sortedEvents.size()){

            List<Event> eventsToAffectToAgents = new ArrayList<>();

            // on crée une liste d'events de la même taille que les agents disponibles
            for (int i = 0; i < agents.size() ; i++) {
                eventsToAffectToAgents.add(sortedEvents.pollFirst());
            }
            System.out.println("eventsToAffectToAgents");
            System.out.println(eventsToAffectToAgents);
            System.out.println("sortedEvents");
            System.out.println(sortedEvents);

            callCenters = agentDao.getCallCenters();
            List<Event> eventsToAffectToCallCenters = new ArrayList<>(sortedEvents);

            if(eventsToAffectToAgents.size()!=0){
                bestSolutions = geneticAffectation.genetics(eventsToAffectToAgents, agents);
                bestSolutionsInteger = extractBestSolutionsIds(bestSolutions);
            }


            Map<String, Integer> eventsCallCenters = mapEventstoCallCenters(eventsToAffectToCallCenters,callCenters);

            bestSolutionsInteger.putAll(eventsCallCenters);

        }
        else {
            bestSolutions = geneticAffectation.genetics(new ArrayList<>(sortedEvents), agents);
            bestSolutionsInteger = extractBestSolutionsIds(bestSolutions);
        }


        return bestSolutionsInteger;
    }

    private Map<String, Integer> extractBestSolutionsIds(Set<Map<Event, Agent>> bestSolution){
        Map<String, Integer> convetedEventAgentIds = new HashMap<>();

        for (Map<Event, Agent> key:
                bestSolution) {

            for (Map.Entry<Event, Agent> entry : key.entrySet())
            {
                System.out.println(entry.getKey() + "/" + entry.getValue());

                Event event = ((Event)entry.getKey());
                String eventID = event.getIdentifiant()+event.getEmplacement();
                Integer agentID = ((Agent)entry.getValue()).getId();

                convetedEventAgentIds.put(eventID, agentID);
            }
        }

        return convetedEventAgentIds;
    }

}
