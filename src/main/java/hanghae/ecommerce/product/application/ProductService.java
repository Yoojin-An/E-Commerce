package hanghae.ecommerce.product.application;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

	public Product createProduct(String name, Integer quantity) {
		validateDuplicate(name);
		Product product = Product.of(name, quantity);
		return productRepository.create(product);
	}

	public Product getProduct(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ObjectOptimisticLockingFailureException.class)
	public Product updateStockWithTransaction(Long productId, Integer quantity, String operation) {
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
