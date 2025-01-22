package ecommerce.ecommerce.presentation;

import ecommerce.ecommerce.business.ProductService;
import ecommerce.ecommerce.domain.Product;
import ecommerce.ecommerce.domain.StockHistory;
import ecommerce.ecommerce.presentation.dto.ProductRequest;
import ecommerce.ecommerce.presentation.dto.StockResponse;
import ecommerce.ecommerce.presentation.dto.StockUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
//@AllArgsConstructor
public class ProductController {
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private final ProductService productService;
    
    @Tag(name = "상품 입고", description = "상품을 등록합니다.")
    @PostMapping
    public ResponseEntity<StockResponse> setProduct(
        @RequestBody ProductRequest productRequest
    ) {
        Product product = productService.createProduct(productRequest.getName(), productRequest.getQuantity());
        return ResponseEntity.ok(StockResponse.of(product));
    }

    @Tag(name = "상품별 재고 조회", description = "특정 상품의 재고를 조회합니다.")
    @GetMapping("/{productId}/stocks")
    public ResponseEntity<StockResponse> getStocks(
        @PathVariable Long productId
        ) {
        Product product = productService.getProduct(productId);
        return ResponseEntity.ok(StockResponse.of(product));
    }

    @Tag(name = "상품별 재고 증감", description = "특정 상품의 재고를 증가 또는 감소시킵니다.")
    @PatchMapping("/{productId}/stocks")
    public ResponseEntity<StockResponse> updateStocks(
        @PathVariable Long productId,
        @RequestBody StockUpdateRequest stockUpdateRequest
    ) {
        Product product = productService.updateStock(productId, stockUpdateRequest.getQuantity(), stockUpdateRequest.getOperation());
        return ResponseEntity.ok(StockResponse.of(product));
    }

    @Tag(name = "상품별 재고 증감 이력 조회", description = "특정 상품의 재고 증감 이력을 조회합니다.")
    @GetMapping("{productId}/stocks/history")
    public ResponseEntity<List<StockHistory>> getStockHistories(
            @PathVariable Long productId
    ) {
        Product product = productService.getProduct(productId);
        return ResponseEntity.ok(product.getStockHistory());
    }
}
