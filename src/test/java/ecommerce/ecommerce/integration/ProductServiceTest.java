package ecommerce.ecommerce.integration;

import ecommerce.ecommerce.business.ProductService;
import ecommerce.ecommerce.domain.Product;
import ecommerce.ecommerce.domain.StockHistoryType;
import ecommerce.ecommerce.testdouble.repository.FakeProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductServiceTest {
    private ProductService productService;

    @BeforeEach
    void setUp() {
        FakeProductRepository productRepository = new FakeProductRepository();
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
            Product product = Product.of("test", stock);
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
