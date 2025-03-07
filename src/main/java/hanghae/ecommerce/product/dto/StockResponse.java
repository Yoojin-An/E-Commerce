package hanghae.ecommerce.product.dto;

import hanghae.ecommerce.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {
	private Long productId;
	private Integer stock;

	public static StockResponse of(Product product) {
		return new StockResponse(product.getId(), product.getStock());
	}
}
