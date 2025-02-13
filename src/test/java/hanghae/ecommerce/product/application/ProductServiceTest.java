package hanghae.ecommerce.product.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import hanghae.ecommerce.infrastructure.fake.FakeProductRepository;
import hanghae.ecommerce.product.domain.Product;
import hanghae.ecommerce.product.domain.StockHistoryType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {
	private ProductService productService;
	private FakeProductRepository productRepository;

	@BeforeAll
	void setUp() {
		this.productRepository = new FakeProductRepository();
		this.productService = new ProductService(productRepository);
		productRepository.clear();
	}

	@Nested
	@DisplayName("상품 등록")
	public class CreateProductTest {
		@Test
		@DisplayName("이름이 중복될 경우 상품 등록에 실패한다")
		void failToCreateProductWhenNameIsDuplicated() {
			// given
			String existsName = "test";
			productService.createProduct(existsName, 5);

			// then
			assertThrows(IllegalArgumentException.class, () -> {
				// when
				productService.createProduct(existsName, 5);
			});
		}
	}

	@Nested
	@DisplayName("상품 증감 이력")
	public class StockHistoryTest {
		@Test
		@DisplayName("상품 증감 이력을 조회한다")
		void getStockHistory() {
			// given
			Integer stock = 5;
			Product product = Product.of(1L, "test", stock);
			productRepository.create(product);

			String increaseOperation = "increase";
			String decreaseOperation = "decrease";
			Integer firstIncreaseQuantity = 10;
			Integer secondIncreaseQuantity = 20;
			Integer firstDecreaseQuantity = 3;
			Integer secondDecreaseQuantity = 10;

			// when
			productService.updateStock(product.getId(), firstIncreaseQuantity, increaseOperation);
			productService.updateStock(product.getId(), secondIncreaseQuantity, increaseOperation);
			productService.updateStock(product.getId(), firstDecreaseQuantity, decreaseOperation);
			productService.updateStock(product.getId(), secondDecreaseQuantity, decreaseOperation);
			product = productRepository.findById(product.getId()).orElseThrow();

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
	}
}
