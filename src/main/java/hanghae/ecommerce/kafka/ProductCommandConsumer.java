package hanghae.ecommerce.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import hanghae.ecommerce.events.Event;
import hanghae.ecommerce.product.application.ProductService;
import hanghae.ecommerce.product.domain.Product;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCommandConsumer {

	private final ProductService productService;
	private final ProductEventProducer eventProducer;

	@KafkaListener(topics = "stock-command", groupId = "stock-group")
	public void onCommandEvent(ConsumerRecord<String, Event> record) {
		Object event = record.value().getEvent();

		if (event instanceof ProductCreatedEvent) {
			handleCreateProduct((ProductCreatedEvent)event);
		} else if (event instanceof StockUpdatedEvent) {
			handleUpdateStock((StockUpdatedEvent)event);
		}
	}

	private void handleCreateProduct(ProductCreatedEvent event) {
		Product product = productService.createProduct(event);

		ProductCreatedEvent result = new ProductCreatedEvent(
			product.getId(),
			product.getName(),
			product.getStock()
		);
		eventProducer.sendResultEvent(result);
	}

	private void handleUpdateStock(StockUpdatedEvent event) {
		Product product = productService.updateStock(event);

		StockUpdatedEvent result = new StockUpdatedEvent(
			product.getId(),
			product.getOperation();
			product.getQuantity();
		);
		eventProducer.sendResultEvent(result);
	}
}
