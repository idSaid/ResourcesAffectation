package fr.ul.m2sid.genetic_affectation.asyncMesseging;

import fr.ul.m2sid.genetic_affectation.entity.Event;
import fr.ul.m2sid.genetic_affectation.service.Mapper;
import javafx.util.Pair;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.TreeSet;

@Service
public class EventReceiver {

    @Autowired
    Mapper mapper;
    
    @Autowired
    AffectationSender affectationSender;
    
    @RabbitListener(queues = RabbiMQConfig.P1_AFFECTATION_QUEUE)
    public void receiveEventStream(TreeSet<Event> events) {
        for (Event event: events) {
            System.out.println("Received <" + event.toString() + ">");
        }
        try {
            Map<Integer, Integer> matchedEvent = mapper.affectEventsGeneticMethod(events);
            for (Map.Entry<Integer, Integer> entry : matchedEvent.entrySet()){
                System.out.println(">>> entry key" + entry.getKey());
                System.out.println(">>> entry value" + entry.getValue());
                affectationSender.sendAllocation(new Pair<Integer, Integer>(entry.getKey(),entry.getValue()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
