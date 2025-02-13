package hanghae.ecommerce.order.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import hanghae.ecommerce.product.domain.Product;

public class OrderTest {
	public OrderTest() {
	}

	@Nested
	@DisplayName("주문 생성")
	public class createOrder {
		@Test
		@DisplayName("주문을 생성한다")
		void successToCreateOrder() {
			// given
			Product product1 = Product.of("test1", 10);
			Product product2 = Product.of("test2", 20);
			Product product3 = Product.of("test3", 30);
			OrderItem orderItem1 = OrderItem.of(product1, 5);
			OrderItem orderItem2 = OrderItem.of(product2, 5);
			OrderItem orderItem3 = OrderItem.of(product3, 5);
			List<OrderItem> orderItems = List.of(orderItem1, orderItem2, orderItem3);

			// when
			Order order = Order.of(orderItems);

			// then
			assertThat(order.getOrderItems().getFirst().getProduct().getName()).isEqualTo(product1.getName());
		}

		@Test
		@DisplayName("주문 아이템 중 하나라도 수량이 0 이하면 주문 생성에 실패한다")
		void failToCreateOrderWhenOrderItemQuantityIsZeroOrBelow() {
			// given
			Product product1 = Product.of("test1", 10);
			Product product2 = Product.of("test2", 20);
			Product product3 = Product.of("test3", 30);
			OrderItem orderItem1 = OrderItem.of(product1, -5);
			OrderItem orderItem2 = OrderItem.of(product2, 5);
			OrderItem orderItem3 = OrderItem.of(product3, 5);
			List<OrderItem> orderItems = List.of(orderItem1, orderItem2, orderItem3);

			// then
			assertThrows(IllegalArgumentException.class, () -> {
				// when
				Order.of(orderItems);
			});
		}
	}
}
