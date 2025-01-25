package ecommerce.ecommerce.business;

import ecommerce.ecommerce.business.repository.OrderRepository;
import ecommerce.ecommerce.domain.Order;
import ecommerce.ecommerce.domain.OrderItem;
import ecommerce.ecommerce.domain.OrderStatus;
import ecommerce.ecommerce.domain.Product;
import ecommerce.ecommerce.presentation.dto.OrderRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final ProductService productService;
    private final OrderRepository orderRepository;

    public OrderService(ProductService productService, OrderRepository orderRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(List<OrderItem> orderItems) {
        Order order = Order.of(orderItems);
        order.modifyStatus(OrderStatus.PROCESSING);
        orderRepository.create(order);

        List<OrderItem> decreasedItems = new ArrayList<>();

        try {
            for (OrderItem orderItem : orderItems) {
                productService.updateStock(orderItem.getProduct().getId(), orderItem.getQuantity(), "decrease");
                decreasedItems.add(orderItem);
            }
            return order;
        } catch (Exception e) {
            cancelOrder(order, decreasedItems);
            throw new RuntimeException("Order creation failed, rolling back", e);
        }
    }

    @Transactional
    public void cancelOrder(Order order, List<OrderItem> decreasedItems) {
        order.modifyStatus(OrderStatus.CANCELED);
        decreasedItems.forEach(item ->
                productService.updateStock(item.getProduct().getId(), item.getQuantity(), "increase")
        );
        orderRepository.update(order);
    }
}
