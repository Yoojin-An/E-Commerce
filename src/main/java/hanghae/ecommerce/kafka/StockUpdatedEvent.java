package hanghae.ecommerce.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class StockUpdatedEvent {
	private final String uuid;
	private final Long productId;
	private final String operation;
	private final int quantity;
}
