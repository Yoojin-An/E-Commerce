package ecommerce.ecommerce.presentation.dto;

import ecommerce.ecommerce.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@AllArgsConstructor
public class ProductRequest {
    private String name;
    private Integer quantity;

    public ProductRequest(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    };

    public String getName() {
        return this.name;
    }

    public Integer getQuantity() {
        return this.quantity;
    }
}
