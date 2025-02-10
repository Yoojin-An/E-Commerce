package hanghae.ecommerce.order.dto;

import java.util.List;

import hanghae.ecommerce.order.domain.Order;
import hanghae.ecommerce.order.domain.OrderItem;

public class OrderResponse {
	private Long orderId;
	private List<OrderItem> orderItems;

	public OrderResponse(Long orderId, List<OrderItem> orderItems) {
		this.orderId = orderId;
		this.orderItems = orderItems;
	}

	public Long getOrderId() {
		return orderId;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public static OrderResponse of(Order order) {
		return new OrderResponse(order.getId(), order.getOrderItems());
	}
}
