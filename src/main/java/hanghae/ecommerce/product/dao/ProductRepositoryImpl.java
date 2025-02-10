package hanghae.ecommerce.product.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import hanghae.ecommerce.product.domain.Product;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {
	private final ProductJpaRepository productJpaRepository;

	@Override
	public Product create(Product product) {
		return productJpaRepository.save(product);
	}

	@Override
	public Product update(Product product) {
		return productJpaRepository.save(product);
	}

	@Override
	public Optional<Product> findById(Long productId) {
		return productJpaRepository.findById(productId);
	}

	@Override
	public Boolean existsByName(String name) {
		return productJpaRepository.existsByName(name);
	}
}
