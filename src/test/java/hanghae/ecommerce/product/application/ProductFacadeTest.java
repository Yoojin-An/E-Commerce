package hanghae.ecommerce.product.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import hanghae.ecommerce.product.dao.ProductJpaRepository;
import hanghae.ecommerce.product.domain.Product;
import hanghae.ecommerce.product.domain.StockHistoryType;

@SpringBootTest
@ActiveProfiles("test")
public class ProductFacadeTest {
	@Autowired
	private ProductFacade productFacade;

	@Autowired
	private ProductJpaRepository productRepository;

	private Product product;
	private Long productId = 1L;
	private Integer stock = 150;

	@BeforeEach
	void setUp() {
		productRepository.deleteAllInBatch();
	}

	@Nested
	@DisplayName("상품 재고 증감")
	public class StockHistoryTest {
		@Test
		@DisplayName("이력을 조회한다")
		void getStockHistory() {
			// given
			product = Product.of("test", stock);
			productRepository.save(product);

			String increaseOperation = "increase";
			String decreaseOperation = "decrease";
			Integer firstIncreaseQuantity = 10;
			Integer secondIncreaseQuantity = 20;
			Integer firstDecreaseQuantity = 3;
			Integer secondDecreaseQuantity = 10;

			// when
			product = productFacade.updateStock(product.getId(), firstIncreaseQuantity, increaseOperation);
			product = productFacade.updateStock(product.getId(), secondIncreaseQuantity, increaseOperation);
			product = productFacade.updateStock(product.getId(), firstDecreaseQuantity, decreaseOperation);
			product = productFacade.updateStock(product.getId(), secondDecreaseQuantity, decreaseOperation);

			// then
			assertThat(product.getStockHistory().get(0).getQuantity()).isEqualTo(firstIncreaseQuantity);
			assertThat(product.getStockHistory().get(1).getQuantity()).isEqualTo(secondIncreaseQuantity);
			assertThat(product.getStockHistory().get(2).getQuantity()).isEqualTo(firstDecreaseQuantity);
			assertThat(product.getStockHistory().get(3).getQuantity()).isEqualTo(secondDecreaseQuantity);
			assertThat(product.getStockHistory().get(0).getType()).isEqualTo(StockHistoryType.INCREASE);
			assertThat(product.getStockHistory().get(1).getType()).isEqualTo(StockHistoryType.INCREASE);
			assertThat(product.getStockHistory().get(2).getType()).isEqualTo(StockHistoryType.DECREASE);
			assertThat(product.getStockHistory().get(3).getType()).isEqualTo(StockHistoryType.DECREASE);
		}

		@Test
		@DisplayName("동시에 100개의 재고 감소 요청에 성공한다")
		void testConcurrentStockUpdate() throws InterruptedException {
			// given
			product = Product.of("test", stock);
			productRepository.save(product);

			int threadCounts = 100;
			Integer quantity = 1;
			ExecutorService executorService = Executors.newFixedThreadPool(threadCounts);
			CountDownLatch latch = new CountDownLatch(threadCounts);
			List<Future<Boolean>> results = new ArrayList<>();

			// when
			for (int i = 0; i < threadCounts; i++) {
				Future<Boolean> future = executorService.submit(() -> {
					try {
						product = productFacade.updateStock(product.getId(), quantity, "decrease");
						return true;
					} catch (OptimisticLockingFailureException e) {
						return false;
					} finally {
						latch.countDown();
					}
				});
				results.add(future);
			}

			latch.await(); // 모든 스레드가 작업을 마칠 때까지 대기
			executorService.shutdown();

			long successCount = results.stream().filter(future -> {
				try {
					return future.get();
				} catch (Exception e) {
					return false;
				}
			}).count();
			Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();

			// then
			assertThat(successCount).isEqualTo(threadCounts);
			assertThat(updatedProduct.getStock()).isEqualTo(stock - quantity * threadCounts);
		}
	}
}


