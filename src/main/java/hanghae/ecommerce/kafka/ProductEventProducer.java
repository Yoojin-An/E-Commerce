package hanghae.ecommerce.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import hanghae.ecommerce.events.Event;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductEventProducer {

	@Autowired
	private final KafkaTemplate<String, Event> kafkaTemplate;

	private static final String COMMAND_TOPIC = "stock-command";
	private static final String RESULT_TOPIC = "stock-result";

	public void sendCommandEvent(Object event) {
		kafkaTemplate.send(COMMAND_TOPIC, new Event(event.getClass().getName(), event));
	}

	public void sendResultEvent(Object event) {
		kafkaTemplate.send(RESULT_TOPIC, new Event(event.getClass().getName(), event));
	}
}
