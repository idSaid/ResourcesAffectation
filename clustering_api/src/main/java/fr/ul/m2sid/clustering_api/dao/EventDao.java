package fr.ul.m2sid.clustering_api.dao;

import fr.ul.m2sid.clustering_api.entity.Event;
import org.springframework.stereotype.Service;

@Service
public interface EventDao {
    void saveEvent(Event event);
}
