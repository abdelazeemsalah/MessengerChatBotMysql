package com.chatbot.services;

import java.util.List;

import com.chatbot.entity.BotButton;
import com.chatbot.entity.BotGTemplateMessage;
import com.chatbot.entity.BotInteraction;
import com.chatbot.entity.BotInteractionMessage;
import com.chatbot.entity.BotQuickReplyMessage;
import com.chatbot.entity.BotTemplateElement;
import com.chatbot.entity.BotTextMessage;
import com.chatbot.entity.BotWebserviceMapping;
import com.chatbot.entity.BotWebserviceMessage;

public interface ChatBotService {
	public List<BotInteractionMessage> findInteractionMessagesByInteractionId(Integer interactionId);

	public BotQuickReplyMessage findQuickReplyMessageByMessageId(Integer messageId);

	public List<BotButton> findButtonsByQuickReplyMessageId(Integer quickMsgId);

	public BotInteraction findInteractionByPayload(String payload);

	public BotTextMessage findTextMessageByMessageId(Integer messageId);

	public BotGTemplateMessage findGTemplateMessageByMessageId(Integer messageId);

	public List<BotTemplateElement> findTemplateElementsByGTMsgId(Integer gTMsgId);

	public List<BotButton> findButtonsByTemplateElementId(Integer elementId);

	public BotWebserviceMessage findWebserviceMessageByMessageId(Integer messageId);

	public List<BotWebserviceMapping> findWebserviceMappingByWsId(Integer wsId);
}
