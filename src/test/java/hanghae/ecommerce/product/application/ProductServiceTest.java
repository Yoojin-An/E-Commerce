package hanghae.ecommerce.product.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import hanghae.ecommerce.infrastructure.fake.FakeProductRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {
	private ProductFacade productFacade;
	private ProductService productService;
	private FakeProductRepository productRepository;

	@BeforeAll
	void setUp() {
		this.productRepository = new FakeProductRepository();
		this.productService = new ProductService(productRepository);
		this.productFacade = new ProductFacade(productService);
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
}
