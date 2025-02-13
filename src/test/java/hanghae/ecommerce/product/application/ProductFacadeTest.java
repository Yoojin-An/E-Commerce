package hanghae.ecommerce.product.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import hanghae.ecommerce.infrastructure.fake.FakeProductRepository;
import hanghae.ecommerce.product.domain.Product;
import hanghae.ecommerce.product.domain.StockHistoryType;

public class ProductFacadeTest {
	private ProductFacade productFacade;
	private ProductService productService;
	private FakeProductRepository productRepository;

	@BeforeEach
	void setUp() {
		this.productRepository = new FakeProductRepository();
		this.productService = new ProductService(productRepository);
		this.productFacade = new ProductFacade(productService);
		productRepository.clear();
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
			productFacade.updateStock(product.getId(), firstIncreaseQuantity, increaseOperation);
			productFacade.updateStock(product.getId(), secondIncreaseQuantity, increaseOperation);
			productFacade.updateStock(product.getId(), firstDecreaseQuantity, decreaseOperation);
			productFacade.updateStock(product.getId(), secondDecreaseQuantity, decreaseOperation);
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
