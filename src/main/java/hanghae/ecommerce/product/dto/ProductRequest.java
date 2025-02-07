package hanghae.ecommerce.product.dto;

import lombok.Getter;

@Getter
//@AllArgsConstructor
public class ProductRequest {
	private String name;
	private Integer quantity;

	public ProductRequest(String name, Integer quantity) {
		this.name = name;
		this.quantity = quantity;
	}

	;

	public String getName() {
		return this.name;
	}

	public Integer getQuantity() {
		return this.quantity;
	}
}
