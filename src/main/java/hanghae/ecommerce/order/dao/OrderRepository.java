package hanghae.ecommerce.order.dao;

import java.util.Optional;

import hanghae.ecommerce.order.domain.Order;

public interface OrderRepository {
	Order create(Order order);

	Order update(Order order);

	Optional<Order> findById(Long orderId);
}
