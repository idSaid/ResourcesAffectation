package fr.ul.m2sid.clustering_api.asyncMesseging;

import fr.ul.m2sid.clustering_api.entity.Event;
import javafx.util.Pair;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.TreeSet;

@Service
public class EventSender {

    private RabbitTemplate rabbitTemplate;

    public EventSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSortedEvent(TreeSet<Event> sortedEvents){
        rabbitTemplate.convertAndSend(RabbiMQConfig.P1_AFFECTATION_QUEUE, sortedEvents);
    }
}
