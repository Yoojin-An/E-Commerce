package hanghae.ecommerce.order.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import hanghae.ecommerce.order.dao.OrderRepository;
import hanghae.ecommerce.order.domain.Order;
import hanghae.ecommerce.order.domain.OrderItem;
import hanghae.ecommerce.order.domain.OrderStatus;
import hanghae.ecommerce.product.application.ProductFacade;
import hanghae.ecommerce.product.application.ProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {
	private final ProductFacade productFacade;
	private final ProductService productService;
	private final OrderRepository orderRepository;

	@Transactional
	public Order createOrder(List<OrderItem> orderItems) {
		Order order = Order.of(orderItems);
		order.modifyStatus(OrderStatus.PROCESSING);
		orderRepository.create(order);

		List<OrderItem> decreasedItems = new ArrayList<>();

		try {
			for (OrderItem orderItem : orderItems) {
				productFacade.updateStock(orderItem.getProduct().getId(), orderItem.getQuantity(), "decrease");
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
			productFacade.updateStock(item.getProduct().getId(), item.getQuantity(), "increase")
		);
		orderRepository.update(order);
	}
}
