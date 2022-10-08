package com.github.alsaghir.worker;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class WorkerApplication {

	private final StringRedisTemplate rs;

	public static void main(String[] args) {
		SpringApplication.run(WorkerApplication.class, args);
	}

	@Bean
	RedisMessageListenerContainer keyExpirationListenerContainer(RedisConnectionFactory connectionFactory) {

		RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
		listenerContainer.setConnectionFactory(connectionFactory);

		listenerContainer.addMessageListener((message, pattern) -> {
			log.info(new String(message.getChannel(), StandardCharsets.UTF_8));
			log.info(new String(message.getBody(), StandardCharsets.UTF_8));
			rs.opsForHash().put("values", new String(message.getBody(), StandardCharsets.UTF_8), Integer.toString(fib(Integer.parseInt(new String(message.getBody(), StandardCharsets.UTF_8)))));

		}, new PatternTopic("insert"));

		return listenerContainer;
	}

	private int fib(int index) {
		if (index < 2) return 1;
		return fib(index - 1) + fib(index - 2);
	}

	@Bean
	CountDownLatch latch(){
		CountDownLatch latch = new CountDownLatch(1);
		new Thread(){
			@Override
			@SneakyThrows(InterruptedException.class)
			public synchronized void run() {
				//Hang this thread until counted down
				latch.await();
			}
		}.start();
		return latch;
	}

}
