package hanghae.ecommerce.product.dao;

import java.util.Optional;

import hanghae.ecommerce.product.domain.Product;

public interface ProductRepository {
	Product create(Product product);

	Product update(Product product);

	Optional<Product> findById(Long productId);

	Boolean existsByName(String name);
}
