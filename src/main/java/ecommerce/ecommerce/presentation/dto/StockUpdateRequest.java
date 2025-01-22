package ecommerce.ecommerce.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockUpdateRequest {
    private String operation;
    private Integer quantity;

    public String getOperation() {
        return this.operation;
    }

    public Integer getQuantity() {
        return this.quantity;
    }
}
