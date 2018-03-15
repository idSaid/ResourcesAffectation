package fr.ul.m2sid.clustering_api.asyncMesseging;

import fr.ul.m2sid.clustering_api.entity.Client;
import fr.ul.m2sid.clustering_api.entity.Event;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

@Service
public class EventReceiver {

    @Autowired
    EventSender eventSender;

    public TreeSet<Event> sortEvent(Set<Event> events){
        for (Event event: events) {
            event.setClient(new Client(event.getIdclient()+event.getEmplacement()));
        }
        return new TreeSet<>(events);
    }

    @RabbitListener(queues = RabbiMQConfig.P1_EVENT_QUEUE)
    public void receiveEventStream(Set<Event> events) {
        for (Event event: events) {
            System.out.println("Received <" + event.toString() + ">");
        }
        TreeSet<Event> sortedEvents = sortEvent(events);
        eventSender.sendSortedEvent(sortedEvents);
    }
}
