package hanghae.ecommerce.cache;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CacheSubscriber implements MessageListener {

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		StringRedisSerializer serializer = new StringRedisSerializer();
		String body = serializer.deserialize(message.getBody());

		assert body != null;
		if (body.contains("Updated product-") || body.contains("Deleted product-")) {
			String cachedKey = body.split("-")[1];
			redisTemplate.delete(cachedKey);
		}
	}
}
