package hanghae.ecommerce.product.application;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import hanghae.ecommerce.annotations.RedissonLock;
import hanghae.ecommerce.cache.CachePublisher;
import hanghae.ecommerce.product.dao.ProductRepository;
import hanghae.ecommerce.product.domain.Product;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;
	private final CachePublisher cachePublisher;
	private final RedisTemplate<String, Product> redisTemplate;
	private static final String PRODUCT_KEY_PREFIX = "product:";

	private void validateDuplicate(String name) {
		if (productRepository.existsByName(name)) {
			throw new IllegalArgumentException("Product name already exists: " + name);
		}
	}

	public Product createProduct(String name, Integer quantity) {
		validateDuplicate(name);
		Product product = Product.of(name, quantity);
		return productRepository.create(product);
	}

	public Product getProduct(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
	}

	@RedissonLock(value = "#stock-3f29c8e4-7b1a-4d5f-9c3e-8a2b6d4e7f10")
	public Product updateStock(Long productId, Integer quantity, String operation) {
		Product product = getProduct(productId);

		if ("increase".equalsIgnoreCase(operation)) {
			product.increaseStock(quantity);
		} else if ("decrease".equalsIgnoreCase(operation)) {
			product.decreaseStock(quantity);
		} else {
			throw new IllegalArgumentException("Invalid operation: " + operation);
		}

		Product updatedProduct = productRepository.update(product);
		String redisKey = PRODUCT_KEY_PREFIX + productId;

		redisTemplate.opsForValue().set(redisKey, updatedProduct);
		cachePublisher.publish(redisKey, "Updated stock of product-" + productId);

		return updatedProduct;
	}
}
