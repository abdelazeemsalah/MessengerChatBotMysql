package com.chatbot.dao;

import org.springframework.data.repository.CrudRepository;

import com.chatbot.entity.BotTextMessage;

public interface BotTextMessageRepo extends CrudRepository<BotTextMessage, Integer> {
	public BotTextMessage findByBotInteractionMessageMessageId(Integer messageId);
}
