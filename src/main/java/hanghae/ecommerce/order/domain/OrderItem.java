package hanghae.ecommerce.order.domain;

import hanghae.ecommerce.product.domain.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	private Integer quantity;

	public OrderItem(Product product, Integer quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public static OrderItem of(Product product, Integer quantity) {
		return new OrderItem(product, quantity);
	}

	public static OrderItem of(Long id, Product product, Integer quantity) {
		OrderItem orderItem = OrderItem.of(product, quantity);
		orderItem.id = id;
		return orderItem;
	}

	public Product getProduct() {
		return product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void validateQuantity() {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than zero.");
		}
	}
}
