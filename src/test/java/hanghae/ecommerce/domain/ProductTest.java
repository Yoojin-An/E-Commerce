package hanghae.ecommerce.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import hanghae.ecommerce.infrastructure.fake.FakeProductRepository;
import hanghae.ecommerce.product.domain.Product;
import hanghae.ecommerce.product.domain.StockHistoryType;

public class ProductTest {
	private final FakeProductRepository productRepository;

	public ProductTest() {
		this.productRepository = new FakeProductRepository();
	}

	@BeforeEach
	void setUp() {
		productRepository.clear();
	}

	@Nested
	@DisplayName("상품 생성")
	public class CreateProductTest {
		@Test
		@DisplayName("상품을 생성한다")
		void createProduct() {
			// given
			String expectedName = "test";
			Integer expectedStock = 5;

			// when
			Product product = Product.of(expectedName, expectedStock);

			// then
			assertThat(product.getName()).isEqualTo(expectedName);
			assertThat(product.getStock()).isEqualTo(expectedStock);
		}

		@Test
		@DisplayName("이름이 null인 경우 상품 생성에 실패한다")
		void failToCreateProductWhenNameIsNull() {
			// given
			String expectedName = null;
			Integer expectedStock = 5;

			// then
			assertThrows(IllegalArgumentException.class, () -> {
				// when
				Product.of(expectedName, expectedStock);
			});
		}

		@Test
		@DisplayName("이름이 빈 문자열인 경우 상품 생성에 실패한다")
		void failToCreateProductWhenNameIsEmpty() {
			// given
			String expectedName = " ";
			Integer expectedStock = 5;

			// then
			assertThrows(IllegalArgumentException.class, () -> {
				// when
				Product.of(expectedName, expectedStock);
			});
		}

		@Test
		@DisplayName("재고가 0 이하인 경우 상품 생성에 실패한다")
		void failToCreateProductWhenStockIsStockIsZeroOrBelow() {
			// given
			String expectedName = "test";
			Integer expectedStock = -5;

			// then
			assertThrows(IllegalArgumentException.class, () -> {
				// when
				Product.of(expectedName, expectedStock);
			});
		}
	}

	@Nested
	@DisplayName("재고 조회")
	public class GetStockTest {
		@Test
		@DisplayName("상품의 재고를 조회한다")
		void getStock() {
			// given
			Integer expectedStock = 5;
			Product product = Product.of("test", expectedStock);
			productRepository.create(product);

			// when
			Integer stock = product.getStock();

			// then
			assertThat(stock).isEqualTo(expectedStock);
		}
	}

	@Nested
	@DisplayName("재고 증감")
	public class UpdateStockTest {
		@Test
		@DisplayName("상품의 재고를 증가시킨다")
		void increaseStock() {
			// given
			Integer stock = 5;
			Product product = Product.of("test", stock);
			productRepository.create(product);
			Integer increaseQuantity = 10;

			// when
			product.increaseStock(increaseQuantity);

			// then
			assertThat(product.getStock()).isEqualTo(stock + increaseQuantity);
		}

		@Test
		@DisplayName("상품의 재고를 감소시킨다")
		void decreaseStock() {
			// given
			Integer stock = 5;
			Product product = Product.of("test", stock);
			productRepository.create(product);
			Integer decreaseQuantity = 3;

			// when
			product.decreaseStock(decreaseQuantity);

			// then
			assertThat(product.getStock()).isEqualTo(stock - decreaseQuantity);
		}

		@Test
		@DisplayName("증감시킬 수량이 0 이하이면 상품의 재고 갱신에 실패한다")
		void failToUpdateStockWhenQuantityIsZeroOrBelow() {
			// given
			Integer stock = 5;
			Product product = Product.of("test", stock);
			productRepository.create(product);
			Integer increaseQuantity = -3;

			// then
			assertThrows(IllegalArgumentException.class, () -> {
				// when
				product.increaseStock(increaseQuantity);
			});
		}

		@Test
		@DisplayName("감소시킬 수량이 재고보다 적으면 상품의 재고 감소에 실패한다")
		void failToDecreaseStockWhenStockIsBelowQuantity() {
			// given
			Integer stock = 5;
			Product product = Product.of("test", stock);
			productRepository.create(product);
			Integer decreaseQuantity = 10;

			// then
			assertThrows(IllegalArgumentException.class, () -> {
				// when
				product.decreaseStock(decreaseQuantity);
			});
		}
	}

	@Nested
	@DisplayName("재고 증감 이력")
	public class StockHistoryTest {
		@Test
		@DisplayName("재고 증감 이력을 추가한다")
		void addStockHistory() {
			// given
			Integer stock = 5;
			Product product = Product.of("test", stock);
			productRepository.create(product);
			StockHistoryType increaseType = StockHistoryType.INCREASE;
			StockHistoryType decreaseType = StockHistoryType.DECREASE;
			Integer firstIncreaseQuantity = 10;
			Integer secondIncreaseQuantity = 20;
			Integer firstDecreaseQuantity = 3;
			Integer secondDecreaseQuantity = 10;

			// when
			product.addStockHistory(firstIncreaseQuantity, increaseType);
			product.addStockHistory(secondIncreaseQuantity, increaseType);
			product.addStockHistory(firstDecreaseQuantity, decreaseType);
			product.addStockHistory(secondDecreaseQuantity, decreaseType);
			productRepository.update(product);

			// then
			assertThat(product.getStockHistory().get(0).getQuantity()).isEqualTo(firstIncreaseQuantity);
			assertThat(product.getStockHistory().get(1).getQuantity()).isEqualTo(secondIncreaseQuantity);
			assertThat(product.getStockHistory().get(2).getQuantity()).isEqualTo(firstDecreaseQuantity);
			assertThat(product.getStockHistory().get(3).getQuantity()).isEqualTo(secondDecreaseQuantity);
			assertThat(product.getStockHistory().get(0).getType()).isEqualTo(increaseType);
			assertThat(product.getStockHistory().get(1).getType()).isEqualTo(increaseType);
			assertThat(product.getStockHistory().get(2).getType()).isEqualTo(decreaseType);
			assertThat(product.getStockHistory().get(3).getType()).isEqualTo(decreaseType);
		}
	}
}
