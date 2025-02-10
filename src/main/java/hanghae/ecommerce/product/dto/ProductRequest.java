package hanghae.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductRequest {
	private String name;
	private Integer quantity;
}
