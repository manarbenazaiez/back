package tn.esprit.spring.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public RabbitMQService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessage(String queueName, Long id) {
        amqpTemplate.convertAndSend(queueName, id);
    }
}
