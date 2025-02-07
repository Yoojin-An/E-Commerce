package hanghae.ecommerce.product.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Builder
@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stock_history")
public class StockHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private StockHistoryType type;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Instant createdAt = Instant.now();

	protected StockHistory() {
	}

	public StockHistory(Integer quantity, StockHistoryType type) {
		this.quantity = quantity;
		this.type = type;
		this.createdAt = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public StockHistoryType getType() {
		return type;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public static StockHistory of(Integer quantity, StockHistoryType type) {
		return new StockHistory(quantity, type);
	}
}
