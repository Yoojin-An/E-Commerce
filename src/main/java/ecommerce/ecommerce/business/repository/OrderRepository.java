package ecommerce.ecommerce.business.repository;

import ecommerce.ecommerce.domain.Order;

import java.util.Optional;

public interface OrderRepository {
    Order create(Order order);
    Order update(Order order);
    Optional<Order> findById(Long orderId);
}
