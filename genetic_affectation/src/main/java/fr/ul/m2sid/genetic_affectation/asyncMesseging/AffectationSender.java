package fr.ul.m2sid.genetic_affectation.asyncMesseging;

import javafx.util.Pair;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AffectationSender {

    private RabbitTemplate rabbitTemplate;

    public AffectationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAllocation(Pair<String,Integer> allocation){
        rabbitTemplate.convertAndSend(RabbiMQConfig.P1_ALLOCATION_QUEUE, allocation);
    }
}
