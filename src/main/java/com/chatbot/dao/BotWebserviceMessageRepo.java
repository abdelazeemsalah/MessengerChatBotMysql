package com.chatbot.dao;

import org.springframework.data.repository.CrudRepository;

import com.chatbot.entity.BotWebserviceMessage;

public interface BotWebserviceMessageRepo extends CrudRepository<BotWebserviceMessage, Integer> {
	public BotWebserviceMessage findByBotInteractionMessageMessageId(Integer messageId);

}
