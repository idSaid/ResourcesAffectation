package fr.ul.m2sid.clustering_api.controller;

import fr.ul.m2sid.clustering_api.dao.EventDao;
import fr.ul.m2sid.clustering_api.dao.ParameterDao;
import fr.ul.m2sid.clustering_api.entity.Client;
import fr.ul.m2sid.clustering_api.entity.Event;
import fr.ul.m2sid.clustering_api.entity.Parametre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class EventsController {

    @Autowired
    ParameterDao parameterDao;

    @Autowired
    EventDao eventDao;

    public TreeSet<Event> sortEvent(Set<Event> events){
        for (Event event: events) {
            event.setClient(new Client(event.getIdclient()+event.getEmplacement()));
        }
        return new TreeSet<>(events);
    }

    @PostMapping(value = "/sortEvents", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<TreeSet<Event>> sortEvents(@RequestBody Set<Event> events) throws IOException {
        TreeSet<Event> sortedEvents = sortEvent(events);
        return new ResponseEntity<>(sortedEvents, HttpStatus.OK);
    }

    @PostMapping(value = "/parametres", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateParameters(@RequestBody Parametre parametre) throws IOException {
        System.out.println(parametre.toString());
        parameterDao.updateParametre(parametre);
        return new ResponseEntity(HttpStatus.OK);
    }


}
