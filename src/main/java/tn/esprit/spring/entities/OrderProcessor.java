package tn.esprit.spring.entities;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tn.esprit.spring.entities.Order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class OrderProcessor {
    private final RabbitTemplate rabbitTemplate;

    public OrderProcessor(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @RabbitListener(queuesToDeclare = @Queue(name = "order_queue"))
        public void processOrder(Order order) {
            if (order.getOrderType() == OrderType.BUY) {
                if (checkAvailability(order)) {
                    executeBuyOrder(order);
                    saveOrderResults(order);
                    generateOrderNotification(order, "L'ordre d'achat a été exécuté avec succès.");
                } else {
                    generateOrderNotification(order, "L'ordre d'achat n'a pas pu être exécuté en raison de la non-disponibilité des actions.");
                }
            } else if (order.getOrderType() == OrderType.SELL) {
                if (checkAvailability(order)) {
                    executeSellOrder(order);
                    saveOrderResults(order);
                    generateOrderNotification(order, "L'ordre de vente a été exécuté avec succès.");
                } else {
                    generateOrderNotification(order, "L'ordre de vente n'a pas pu être exécuté en raison de la non-disponibilité des actions.");
                }
            } else {
                generateOrderNotification(order, "Le type d'ordre n'est pas valide.");
            }
        }

    private boolean checkAvailability(Order order) {
        // Placeholder logic to check the availability of stocks
        // Assume that the stock availability is stored in a Map or any other data structure

        // Here's a sample implementation using a Map
        Map<String, Integer> stockAvailability = new HashMap<>();
        stockAvailability.put("AAPL", 100); // Example: 100 units of AAPL stock available

        // Get the available quantity for the given stock symbol
        Integer availableQuantity = stockAvailability.get(order.getStockSymbol());

        // Compare the available quantity with the order quantity
        return availableQuantity != null && availableQuantity >= order.getQuantity();
    }


    private void executeBuyOrder(Order order) {
        // Exécution de l'ordre d'achat
        double totalPrice = order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())).doubleValue();
        System.out.println("Achat effectué : " + order.getQuantity() + " actions de " + order.getStockSymbol() + " pour un total de " + totalPrice);
    }

    private void executeSellOrder(Order order) {
        // Exécution de l'ordre de vente
        double totalPrice = order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())).doubleValue();
        System.out.println("Vente effectuée : " + order.getQuantity() + " actions de " + order.getStockSymbol() + " pour un total de " + totalPrice);
    }

    private void saveOrderResults(Order order) {
        // Enregistrement des résultats de l'ordre
        System.out.println("Résultats de l'ordre enregistrés : " + order.getId());
    }

    private void generateOrderNotification(Order order, String message) {
        // Génération de la notification
        String notification = "Ordre ID: " + order.getId() + "\n"
                + "Stock: " + order.getStockSymbol() + "\n"
                + "Message: " + message;

        // Send the notification as an SMS
       // String recipientPhoneNumber = "+21658101122"; // Specify the recipient's phone number
       // SmsSender.sendSms(recipientPhoneNumber, notification);

       // System.out.println("Notification générée et envoyée par SMS.");
    }

}
