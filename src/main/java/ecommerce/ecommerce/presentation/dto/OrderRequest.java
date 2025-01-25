package ecommerce.ecommerce.presentation.dto;

import ecommerce.ecommerce.domain.OrderItem;

import java.util.List;

public class OrderRequest {
    private List<OrderItem> orderItem;

    public OrderRequest(List<OrderItem> orderItems) {
        this.orderItem = orderItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItem;
    }
}
