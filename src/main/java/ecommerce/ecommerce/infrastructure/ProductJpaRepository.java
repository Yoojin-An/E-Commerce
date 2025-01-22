package ecommerce.ecommerce.infrastructure;

import ecommerce.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ProductJpaRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);
}
