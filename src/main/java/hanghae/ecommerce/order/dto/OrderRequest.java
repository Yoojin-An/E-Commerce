package hanghae.ecommerce.order.dto;

import java.util.List;

import hanghae.ecommerce.order.domain.OrderItem;

public class OrderRequest {
	private List<OrderItem> orderItem;

	public OrderRequest(List<OrderItem> orderItems) {
		this.orderItem = orderItems;
	}

	public List<OrderItem> getOrderItems() {
		return orderItem;
	}
}
