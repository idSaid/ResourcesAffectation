package fr.ul.m2sid.genetic_affectation.controller;

import fr.ul.m2sid.genetic_affectation.GeneticAffectationApplication;
import fr.ul.m2sid.genetic_affectation.dao.AgentDao;
import fr.ul.m2sid.genetic_affectation.entity.Agent;
import fr.ul.m2sid.genetic_affectation.entity.Event;
import fr.ul.m2sid.genetic_affectation.geneticAffectation.GeneticAffectation;
import fr.ul.m2sid.genetic_affectation.service.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class AffectationController {

    @Autowired
    private AgentDao agentDao;

    @Autowired
    private GeneticAffectation geneticAffectation;

    @Autowired
    private Mapper mapper;

    @PostMapping(value = "/affectEvents", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<Integer, Integer>> affectEventsGeneticMethod(@RequestBody TreeSet<Event> sortedEvents) throws IOException {

        ArrayList<Agent> agents = new ArrayList<Agent>(agentDao.getFreeAgents());
        System.out.println(agents);
        Set<Map<Event, Agent>> bestSolutions;
        Map<Integer, Integer> bestSolutionsInteger= new HashMap<>();
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


            Map<Integer, Integer> eventsCallCenters = mapper.mapEventstoCallCenters(eventsToAffectToCallCenters,callCenters);

            bestSolutionsInteger.putAll(eventsCallCenters);

        }
        else {
            bestSolutions = geneticAffectation.genetics(new ArrayList<>(sortedEvents), agents);
            bestSolutionsInteger = extractBestSolutionsIds(bestSolutions);
        }


        return new ResponseEntity<>(bestSolutionsInteger, HttpStatus.OK);
    }

    private Map<Integer, Integer> extractBestSolutionsIds(Set<Map<Event, Agent>> bestSolution){
        Map<Integer, Integer> convetedEventAgentIds = new HashMap<>();

        for (Map<Event, Agent> key:
                bestSolution) {

            for (Map.Entry<Event, Agent> entry : key.entrySet())
            {
                System.out.println(entry.getKey() + "/" + entry.getValue());

                Event event = ((Event)entry.getKey());
                Integer eventID = event.getIdentifiant();
                Integer agentID = ((Agent)entry.getValue()).getId();

                convetedEventAgentIds.put(eventID, agentID);
            }
        }

        return convetedEventAgentIds;
    }
}
