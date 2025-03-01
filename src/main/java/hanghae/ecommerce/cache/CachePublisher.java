package hanghae.ecommerce.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CachePublisher {

	private final RedisTemplate<String, Object> redisTemplate;

	public void publish(String channel, String message) {
		redisTemplate.convertAndSend(channel, message);
	}
}
