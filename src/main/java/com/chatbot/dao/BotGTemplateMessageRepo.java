package com.chatbot.dao;

import org.springframework.data.repository.CrudRepository;

import com.chatbot.entity.BotGTemplateMessage;

public interface BotGTemplateMessageRepo extends CrudRepository<BotGTemplateMessage, Integer> {
	public BotGTemplateMessage findByBotInteractionMessageMessageId(Integer messageId);
}
