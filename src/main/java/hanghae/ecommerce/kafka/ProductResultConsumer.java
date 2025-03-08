package hanghae.ecommerce.kafka;

import java.time.LocalDateTime;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import hanghae.ecommerce.events.Event;
import hanghae.ecommerce.events.EventRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductResultConsumer {

	private final EventRepository eventRepository;
	private final ObjectMapper mapper = new ObjectMapper();

	@KafkaListener(topics = "stock-result", groupId = "stock-group")
	public void onCommandEvent(ConsumerRecord<String, Event> record) {
		Object event = record.value().getEvent();

		if (event instanceof ProductCreatedEvent) {
			handleStockCreated((ProductCreatedEvent)event);
		} else if (event instanceof StockUpdatedEvent) {
			handleStockUpdated((StockUpdatedEvent)event);
		}
	}

	private void handleStockCreated(ProductCreatedEvent evt) {
		Event entity = Event.builder()
			.eventType("ProductCreatedEvent")
			.payload("id=" + evt.getId())
			.eventTime(LocalDateTime.now())
			.status("SUCCESS")
			.build();
		eventRepository.save(entity);
	}

	private void handleStockUpdated(StockUpdatedEvent evt) {
		Event entity = Event.builder()
			.eventType("StockUpdatedEvent")
			.payload("stockId=" + evt.getId())
			.eventTime(LocalDateTime.now())
			.status("SUCCESS")
			.build();
		eventRepository.save(entity);
	}
}
