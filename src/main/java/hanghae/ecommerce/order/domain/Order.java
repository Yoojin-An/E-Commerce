package hanghae.ecommerce.order.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`order`")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	private List<OrderItem> orderItems = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status = OrderStatus.PENDING;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Instant createdAt = Instant.now();

	@LastModifiedDate
	private Instant updatedAt = null;

	private Instant deletedAt = null;

	private Instant completedAt = null;

	public Order() {
	}

	public Order(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public static Order of(List<OrderItem> orderItems) {
		orderItems.forEach(OrderItem::validateQuantity);
		return new Order(orderItems);
	}

	public static Order of(Long id, List<OrderItem> orderItems) {
		Order order = Order.of(orderItems);
		order.id = id;
		return order;
	}

	//    public Long getId() { return id; }

	//    public List<OrderItem> getOrderItems() { return orderItems; }

	public Order complete() {
		status = OrderStatus.COMPLETED;
		this.completedAt = Instant.now();
		return this;
	}

	public Order modifyItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
		return this;
	}

	public OrderStatus modifyStatus(OrderStatus status) {
		this.status = status;
		return status;
	}

	public void cancel() {
		status = OrderStatus.CANCELED;
		deletedAt = Instant.now();
	}
}

