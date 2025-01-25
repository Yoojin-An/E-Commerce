package ecommerce.ecommerce.presentation.dto;

import ecommerce.ecommerce.domain.Order;
import ecommerce.ecommerce.domain.OrderItem;

import java.util.List;

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