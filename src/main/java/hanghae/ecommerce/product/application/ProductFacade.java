package hanghae.ecommerce.product.application;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import hanghae.ecommerce.product.domain.Product;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductFacade {
	private final ProductService productService;

	@Retryable(
		retryFor = ObjectOptimisticLockingFailureException.class,
		maxAttempts = 5,
		backoff = @Backoff(delay = 500)
	)
	public Product updateStock(Long productId, Integer quantity, String operation) {
		return productService.updateStockWithTransaction(productId, quantity, operation);
	}
}
