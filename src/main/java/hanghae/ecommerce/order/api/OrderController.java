package hanghae.ecommerce.order.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hanghae.ecommerce.order.application.OrderService;
import hanghae.ecommerce.order.domain.Order;
import hanghae.ecommerce.order.dto.OrderRequest;
import hanghae.ecommerce.order.dto.OrderResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/orders")
public class OrderController {
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@Tag(name = "주문 생성", description = "주문을 생성합니다.")
	@PostMapping
	public ResponseEntity<OrderResponse> createOrder(
		@RequestBody OrderRequest orderRequest
	) {
		Order order = orderService.createOrder(orderRequest.getOrderItems());
		return ResponseEntity.ok(OrderResponse.of(order));
	}
}
