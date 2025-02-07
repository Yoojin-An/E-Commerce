package hanghae.ecommerce.product.application;

import org.springframework.stereotype.Service;

import hanghae.ecommerce.product.dao.ProductRepository;
import hanghae.ecommerce.product.domain.Product;
import jakarta.persistence.EntityNotFoundException;

@Service
//@AllArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

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

	public Product updateStock(Long productId, Integer quantity, String operation) {
		Product product = getProduct(productId);

		if ("increase".equalsIgnoreCase(operation)) {
			product.increaseStock(quantity);
		} else if ("decrease".equalsIgnoreCase(operation)) {
			product.decreaseStock(quantity);
		} else {
			throw new IllegalArgumentException("Invalid operation: " + operation);
		}

		return productRepository.update(product);
	}
}
