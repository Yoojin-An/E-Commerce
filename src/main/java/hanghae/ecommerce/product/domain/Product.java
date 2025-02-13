package hanghae.ecommerce.product.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "stock", nullable = false)
	private Integer stock;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "product_id")
	@Builder.Default
	private List<StockHistory> stockHistory = new ArrayList<>();

	public Product(String name, Integer stock) {
		this.name = name;
		this.stock = stock;
		this.stockHistory = new ArrayList<>();
	}

	public static Product of(String name, Integer stock) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Product name cannot be null or empty.");
		}
		if (stock == null || stock <= 0) {
			throw new IllegalArgumentException("Product Stock must be greater than zero.");
		}
		return new Product(name, stock);
	}

	public static Product of(Long id, String name, Integer stock) {
		Product product = Product.of(name, stock);
		product.id = id;
		return product;
	}

	public void increaseStock(Integer quantity) {

		if (quantity == null || quantity <= 0) {
			throw new IllegalArgumentException("Invalid quantity to increase stock");
		}
		this.stock += quantity;
		addStockHistory(quantity, StockHistoryType.INCREASE);
	}

	public void decreaseStock(Integer quantity) {
		if (quantity == null || quantity <= 0) {
			throw new IllegalArgumentException("Invalid quantity to decrease stock");
		}

		if (this.stock < quantity) {
			throw new IllegalArgumentException("Not enough stock to decrease");
		}
		this.stock -= quantity;
		addStockHistory(quantity, StockHistoryType.DECREASE);
	}

	public void addStockHistory(Integer quantity, StockHistoryType type) {
		StockHistory stockHistory = StockHistory.of(quantity, type);
		this.stockHistory.add(stockHistory);
	}
}
