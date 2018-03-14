package com.chatbot.dao;

import org.springframework.data.repository.CrudRepository;

import com.chatbot.entity.BotQuickReplyMessage;

public interface BotQuickReplyMessageRepo extends CrudRepository<BotQuickReplyMessage, Integer> {
	public BotQuickReplyMessage findByBotInteractionMessageMessageId(Integer messageId);
}
