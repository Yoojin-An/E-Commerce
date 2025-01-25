package ecommerce.ecommerce.infrastructure;

import ecommerce.ecommerce.business.repository.OrderRepository;
import ecommerce.ecommerce.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order create(Order order) { return orderJpaRepository.save(order); }

    @Override
    public Order update(Order order) { return orderJpaRepository.save(order); }

    @Override
    public Optional<Order> findById(Long orderId) { return orderJpaRepository.findById(orderId); }
}
