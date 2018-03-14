package com.chatbot.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.chatbot.entity.BotInteractionMessage;

public interface ChatBotRepository extends CrudRepository<BotInteractionMessage, Integer> {

	public List<BotInteractionMessage> findByBotInteractionInteractionId(Integer interactionId);
}
