package hanghae.ecommerce.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ProductCreatedEvent {
	private final String uuid;
	private final String name;
	private final int quantity;
}
