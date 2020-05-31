package com.example.messagingredis;

import com.example.messagingredis.receiver.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@SpringBootApplication
public class MessagingRedisApplication {
	public static final Logger logger = LoggerFactory.getLogger(MessagingRedisApplication.class);

	public static void main(String[] args) throws InterruptedException{
		ApplicationContext context =
			SpringApplication.run(MessagingRedisApplication.class, args);

		StringRedisTemplate redisTemplate = context.getBean(StringRedisTemplate.class);
		Receiver receiver = context.getBean(Receiver.class);

		while (receiver.getCounter() == 0) {
			logger.info("Sending messages...");
			redisTemplate.convertAndSend("chat", "Hello from Redis!");
			Thread.sleep(500L);
		}
		System.exit(0);
	}

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
											MessageListenerAdapter adapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(adapter, new PatternTopic("chat"));
		return container;
	}

	@Bean
	MessageListenerAdapter adapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	Receiver receiver() {
		return new Receiver();
	}

	@Bean
	StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}


}
