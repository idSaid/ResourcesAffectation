package fr.ul.m2sid.genetic_affectation.asyncMesseging;

import fr.ul.m2sid.genetic_affectation.entity.Allocation;
import javafx.util.Pair;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AffectationSender {

    private RabbitTemplate rabbitTemplate;

    public AffectationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAllocation(Pair<Integer,Integer> allocation){
        System.out.println(">>> sent :" + allocation);
        rabbitTemplate.convertAndSend(RabbiMQConfig.P1_ALLOCATION_QUEUE, new Allocation(allocation.getValue(),allocation.getKey()));
    }
}
