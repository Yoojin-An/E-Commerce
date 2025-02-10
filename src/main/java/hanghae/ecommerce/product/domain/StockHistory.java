package hanghae.ecommerce.product.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	public StockHistory(Integer quantity, StockHistoryType type) {
		this.quantity = quantity;
		this.type = type;
		this.createdAt = Instant.now();
	}

	public static StockHistory of(Integer quantity, StockHistoryType type) {
		return new StockHistory(quantity, type);
	}
}
