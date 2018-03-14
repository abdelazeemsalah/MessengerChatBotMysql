package com.chatbot.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.chatbot.entity.BotInteractionMessage;

public interface InteractionMessageRepo extends CrudRepository<BotInteractionMessage, Integer> {
	public List<BotInteractionMessage> findByBotInteractionInteractionIdOrderByMessagePriority(Integer interactionId);
}
