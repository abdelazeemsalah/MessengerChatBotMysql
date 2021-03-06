package com.chatbot.dao;

import org.springframework.data.repository.CrudRepository;

import com.chatbot.entity.BotInteraction;

public interface BotInteractionRepo extends CrudRepository<BotInteraction, Integer> {
	public BotInteraction findByPayload(String payload);
}
