package fr.ul.m2sid.genetic_affectation.service;

import fr.ul.m2sid.genetic_affectation.entity.Agent;
import fr.ul.m2sid.genetic_affectation.entity.Event;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class Mapper {

    public Map<Integer, Integer> mapEventstoCallCenters(List<Event> eventsToAffect, Set<Agent> callCenters) {
        Map<Integer, Integer> solution = new HashMap<>();
        Integer idRandomCallCenter = callCenters.iterator().next().getId();
        for (Event event: eventsToAffect) {
            solution.put(event.getIdentifiant(),idRandomCallCenter);
        }
        return solution;
    }

}
