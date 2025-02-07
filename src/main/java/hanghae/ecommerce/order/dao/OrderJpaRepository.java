package hanghae.ecommerce.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hanghae.ecommerce.order.domain.Order;

interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
