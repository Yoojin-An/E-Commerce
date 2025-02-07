package hanghae.ecommerce.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import hanghae.ecommerce.product.domain.Product;

interface ProductJpaRepository extends JpaRepository<Product, Long> {
	Boolean existsByName(String name);
}
