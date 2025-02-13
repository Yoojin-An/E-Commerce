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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`order`")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	@Builder.Default
	private List<OrderItem> orderItems = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private OrderStatus status = OrderStatus.PENDING;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	@Builder.Default
	private Instant createdAt = Instant.now();

	@LastModifiedDate
	@Builder.Default
	private Instant updatedAt = null;

	@Builder.Default
	private Instant deletedAt = null;

	@Builder.Default
	private Instant completedAt = null;

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

	public Order complete() {
		status = OrderStatus.COMPLETED;
		this.completedAt = Instant.now();
		return this;
	}

	public Order modifyItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
		return this;
	}

	public void modifyStatus(OrderStatus status) {
		this.status = status;
	}

	public void cancel() {
		status = OrderStatus.CANCELED;
		deletedAt = Instant.now();
	}
}

