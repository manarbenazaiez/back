package tn.esprit.spring.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.entities.Order;
import tn.esprit.spring.entities.OrderProcessor;
import tn.esprit.spring.services.OrderService;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
public class BourseController {

    private final OrderService orderService;
    private final AmqpTemplate amqpTemplate;
    private final OrderProcessor orderProcessor;

    @Autowired
    public BourseController(OrderService orderService, AmqpTemplate amqpTemplate, OrderProcessor orderProcessor) {
        this.orderService = orderService;
        this.amqpTemplate = amqpTemplate;
        this.orderProcessor = orderProcessor;
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        // Place the order
        Order placedOrder = orderService.placeOrder(order);

        // Send the order to the RabbitMQ queue
        amqpTemplate.convertAndSend("order_queue", placedOrder);

        // Process the order using the OrderProcessor
        orderProcessor.processOrder(placedOrder);

        return ResponseEntity.ok(placedOrder);
    }

}
