package hanghae.ecommerce.order.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import hanghae.ecommerce.infrastructure.fake.FakeOrderRepository;
import hanghae.ecommerce.infrastructure.fake.FakeProductRepository;
import hanghae.ecommerce.order.domain.OrderItem;
import hanghae.ecommerce.product.application.ProductFacade;
import hanghae.ecommerce.product.application.ProductService;
import hanghae.ecommerce.product.domain.Product;

public class OrderServiceTest {
	private FakeProductRepository productRepository;
	private FakeOrderRepository orderRepository;
	private ProductFacade productFacade;
	private ProductService productService;
	private OrderService orderService;

	@BeforeEach
	void setUp() {
		productRepository = new FakeProductRepository();
		orderRepository = new FakeOrderRepository();
		productService = new ProductService(productRepository);
		orderService = new OrderService(productFacade, productService, orderRepository);
		productRepository.clear();
		orderRepository.clear();
	}

	@Test
	@DisplayName("재고 부족으로 주문이 실패하면 재고는 변하지 않는다")
	void failToCreateOrderWhenStockNotEnough() {
		// given
		Product product = Product.of("Product A", 3);
		productRepository.create(product);

		OrderItem orderItem = OrderItem.of(product, 5);
		List<OrderItem> orderItems = List.of(orderItem);

		// when
		assertThrows(RuntimeException.class, () -> orderService.createOrder(orderItems));

		// then
		assertThat(product.getStock()).isEqualTo(3);
	}

	@Test
	@DisplayName("일부 상품의 재고가 감소한 후 예외 발생 시, 감소된 재고만 복구된다")
	void failToCreateOrderAfterPartialStockDecrease() {
		// given
		Product product1 = Product.of("Product A", 10);
		Product product2 = Product.of("Product B", 5);
		productRepository.create(product1);
		productRepository.create(product2);

		OrderItem orderItem1 = OrderItem.of(product1, 2);
		OrderItem orderItem2 = OrderItem.of(product2, 6);
		List<OrderItem> orderItems = List.of(orderItem1, orderItem2);

		// when
		assertThrows(RuntimeException.class, () -> orderService.createOrder(orderItems));

		// then
		assertThat(product1.getStock()).isEqualTo(10); // 감소 후 복구
		assertThat(product2.getStock()).isEqualTo(5); // 감소되지 않음
	}
}
