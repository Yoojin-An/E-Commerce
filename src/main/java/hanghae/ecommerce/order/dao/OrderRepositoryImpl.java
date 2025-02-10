package hanghae.ecommerce.order.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import hanghae.ecommerce.order.domain.Order;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
	private final OrderJpaRepository orderJpaRepository;

	@Override
	public Order create(Order order) {
		return orderJpaRepository.save(order);
	}

	@Override
	public Order update(Order order) {
		return orderJpaRepository.save(order);
	}

	@Override
	public Optional<Order> findById(Long orderId) {
		return orderJpaRepository.findById(orderId);
	}
}
