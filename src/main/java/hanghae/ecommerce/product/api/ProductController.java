package hanghae.ecommerce.product.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hanghae.ecommerce.kafka.ProductCreatedEvent;
import hanghae.ecommerce.kafka.ProductEventProducer;
import hanghae.ecommerce.kafka.StockUpdatedEvent;
import hanghae.ecommerce.product.application.ProductService;
import hanghae.ecommerce.product.domain.Product;
import hanghae.ecommerce.product.domain.StockHistory;
import hanghae.ecommerce.product.dto.ProductRequest;
import hanghae.ecommerce.product.dto.StockResponse;
import hanghae.ecommerce.product.dto.StockUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("products")
@AllArgsConstructor
public class ProductController {

	private final ProductEventProducer productEventProducer;
	private final ProductService productService;

	@Tag(name = "상품 입고", description = "상품을 등록합니다.")
	@PostMapping
	public String setProduct(
		@RequestBody ProductRequest productRequest
	) {
		String uuid = UUID.randomUUID().toString();
		ProductCreatedEvent event = new ProductCreatedEvent(
			uuid,
			productRequest.getName(),
			productRequest.getQuantity()
		);
		productEventProducer.sendCommandEvent(event);
		return uuid;
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
	public String updateStocks(
		@PathVariable Long productId,
		@RequestBody StockUpdateRequest stockUpdateRequest
	) {
		String uuid = UUID.randomUUID().toString();
		StockUpdatedEvent event = new StockUpdatedEvent(
			uuid,
			productId,
			stockUpdateRequest.getOperation(),
			stockUpdateRequest.getQuantity()
		);
		productEventProducer.sendCommandEvent(event);
		return uuid;
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
