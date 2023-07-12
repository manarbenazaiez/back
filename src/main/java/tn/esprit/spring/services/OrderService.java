package tn.esprit.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Order;
import tn.esprit.spring.entities.OrderType;
import tn.esprit.spring.repositories.OrderRepository;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order placeOrder(Order order) {
        // Save the order to the database
        Order placedOrder = orderRepository.save(order);

        // Process the order
        processOrder(placedOrder);

        return placedOrder;
    }

    private void processOrder(Order order) {
        List<Order> matchingOrders = orderRepository.findByOrderTypeAndStockSymbolOrderByPriceAsc(
                order.getOrderType() == OrderType.BUY ? OrderType.SELL : OrderType.BUY,
                order.getStockSymbol()
        );

        for (Order matchingOrder : matchingOrders) {
            if (matchingOrder.getQuantity() <= order.getQuantity()) {
                // Execute the trade for the matching order
                executeTrade(matchingOrder, order);

                // Adjust the remaining quantity for the current order
                order.setQuantity(order.getQuantity() - matchingOrder.getQuantity());

                // Remove the matching order from the database
                orderRepository.delete(matchingOrder);
            } else {
                // Execute a partial trade for the current order
                executeTrade(order, matchingOrder);

                // Adjust the remaining quantity for the matching order
                matchingOrder.setQuantity(matchingOrder.getQuantity() - order.getQuantity());

                // Save the updated matching order to the database
                orderRepository.save(matchingOrder);

                break;
            }
        }
    }

    private void executeTrade(Order buyOrder, Order sellOrder) {
        // Perform the trade logic here
        // For example, update account balances, update stock quantities, etc.
    }

}

