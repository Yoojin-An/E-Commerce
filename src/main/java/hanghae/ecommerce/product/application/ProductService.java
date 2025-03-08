package hanghae.ecommerce.product.application;

import org.springframework.stereotype.Service;

import hanghae.ecommerce.kafka.ProductCreatedEvent;
import hanghae.ecommerce.kafka.StockUpdatedEvent;
import hanghae.ecommerce.product.dao.ProductRepository;
import hanghae.ecommerce.product.domain.Product;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	private void validateDuplicate(String name) {
		if (productRepository.existsByName(name)) {
			throw new IllegalArgumentException("Product name already exists: " + name);
		}
	}

	public Product createProduct(ProductCreatedEvent event) {
		validateDuplicate(event.getName());
		Product product = Product.of(event.getName(), event.getQuantity());
		return productRepository.create(product);
	}

	public Product getProduct(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
	}

	public Product updateStock(StockUpdatedEvent event) {
		Product product = getProduct(event.getProductId());

		if ("increase".equalsIgnoreCase(event.getOperation())) {
			product.increaseStock(event.getQuantity());
		} else if ("decrease".equalsIgnoreCase(event.getOperation())) {
			product.decreaseStock(event.getQuantity());
		} else {
			throw new IllegalArgumentException("Invalid operation: " + event.getOperation());
		}

		return productRepository.update(product);
	}
}
