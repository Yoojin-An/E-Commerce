package hanghae.ecommerce.aop;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import hanghae.ecommerce.annotations.RedissonLock;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

	private final RedissonClient redissonClient;

	@Around("@annotation(hanghae.ecommerce.annotations.RedissonLock)")
	public Object redissonLock(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		RedissonLock annotation = method.getAnnotation(RedissonLock.class);
		String lockKey = annotation.value();

		RLock lock = redissonClient.getLock(lockKey);

		boolean lockable = false;

		try {
			lockable = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), TimeUnit.MILLISECONDS);
			if (!lockable) {
				throw new RuntimeException("Lock을 획득하지 못했습니다.");
			}

			if (TransactionSynchronizationManager.isActualTransactionActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCompletion(int status) {
						if (lock.isHeldByCurrentThread()) {
							lock.unlock();
						}
					}
				});
			}

			return joinPoint.proceed();

		} catch (IllegalStateException e) { // 언제 IllegalStateException 이 발생하지?
			throw e;
		} finally {
			if (TransactionSynchronizationManager.isSynchronizationActive() && lockable) {
				lock.unlock();
			}
		}
	}
}
