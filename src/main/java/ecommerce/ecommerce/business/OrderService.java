package ecommerce.ecommerce.business;

import ecommerce.ecommerce.business.repository.OrderRepository;
import ecommerce.ecommerce.domain.Order;
import ecommerce.ecommerce.domain.OrderItem;
import ecommerce.ecommerce.domain.OrderStatus;
import ecommerce.ecommerce.domain.Product;
import ecommerce.ecommerce.presentation.dto.OrderRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        orderItems.forEach(orderItem -> {
            Product product = orderItem.getProduct();
            productService.updateStock(product.getId(), orderItem.getQuantity(), "decrease");
        });

        Order order = Order.of(orderItems);
        order.modifyStatus(OrderStatus.PROCESSING);

        return orderRepository.create(order);
    }

//    public void cancelOrder(Long orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
//        order.modifyStatus(OrderStatus.CANCELED);
//        orderRepository.update(order);
//    }
}
