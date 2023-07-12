package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Order;
import tn.esprit.spring.entities.OrderType;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderTypeAndStockSymbolOrderByPriceAsc(OrderType orderType, String stockSymbol);
}
