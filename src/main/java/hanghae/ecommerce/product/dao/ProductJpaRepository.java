package hanghae.ecommerce.product.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import hanghae.ecommerce.product.domain.Product;
import jakarta.persistence.LockModeType;

interface ProductJpaRepository extends JpaRepository<Product, Long> {

	@Lock(LockModeType.OPTIMISTIC)
	Optional<Product> findById(Long productId);

	Boolean existsByName(String name);
}
