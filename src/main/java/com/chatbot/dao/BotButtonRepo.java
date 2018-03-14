package com.chatbot.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.chatbot.entity.BotButton;

public interface BotButtonRepo extends CrudRepository<BotButton, Integer> {
	public List<BotButton> findByBotQuickReplyMessageQuickMsgId(Integer quickMsgId);

	public List<BotButton> findByBotTemplateElementElementId(Integer elementId);

}
