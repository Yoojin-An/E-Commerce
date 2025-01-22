package ecommerce.ecommerce.presentation.dto;

import ecommerce.ecommerce.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
//@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {
    private Long productId;
    private Integer stock;

    public StockResponse(Long productId, Integer stock) {
        this.productId = productId;
        this.stock = stock;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getStock() {
        return stock;
    }

    public static StockResponse of(Product product) {
        return new StockResponse(product.getId(), product.getStock());
    }
}
