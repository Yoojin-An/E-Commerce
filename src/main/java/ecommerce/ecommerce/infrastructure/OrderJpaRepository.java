package ecommerce.ecommerce.infrastructure;

import ecommerce.ecommerce.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
