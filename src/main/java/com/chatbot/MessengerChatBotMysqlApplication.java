package com.chatbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.github.messenger4j.Messenger;

@SpringBootApplication
@ComponentScan("com.chatbot.*")
public class MessengerChatBotMysqlApplication {
	private static final Logger logger = LoggerFactory.getLogger(MessengerChatBotMysqlApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MessengerChatBotMysqlApplication.class, args);
	}

	@Bean
	public Messenger messengerSendClient(@Value("${messenger4j.appSecret}") final String appSecret,
			@Value("${messenger4j.pageAccessToken}") final String pageAccessToken, @Value("${messenger4j.verifyToken}") final String verifyToken) {
		logger.debug("Initializing MessengerSendClient - pageAccessToken: {}", pageAccessToken);
		return Messenger.create(pageAccessToken, appSecret, verifyToken);
	}
}
