package com.chatbot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatbot.dao.BotButtonRepo;
import com.chatbot.dao.BotGTemplateMessageRepo;
import com.chatbot.dao.BotInteractionRepo;
import com.chatbot.dao.BotQuickReplyMessageRepo;
import com.chatbot.dao.BotTemplateElementRepo;
import com.chatbot.dao.BotTextMessageRepo;
import com.chatbot.dao.BotWebserviceMappingRepo;
import com.chatbot.dao.BotWebserviceMessageRepo;
import com.chatbot.dao.InteractionMessageRepo;
import com.chatbot.entity.BotButton;
import com.chatbot.entity.BotGTemplateMessage;
import com.chatbot.entity.BotInteraction;
import com.chatbot.entity.BotInteractionMessage;
import com.chatbot.entity.BotQuickReplyMessage;
import com.chatbot.entity.BotTemplateElement;
import com.chatbot.entity.BotTextMessage;
import com.chatbot.entity.BotWebserviceMapping;
import com.chatbot.entity.BotWebserviceMessage;

@Service
public class ChatBotServiceImpl implements ChatBotService {
	@Autowired
	private InteractionMessageRepo interactionMessageRepo;

	@Autowired
	private BotQuickReplyMessageRepo botQuickReplyMessageRepo;

	@Autowired
	private BotButtonRepo botButtonRepo;

	@Autowired
	private BotInteractionRepo botInteractionRepo;

	@Autowired
	private BotTextMessageRepo botTextMessageRepo;

	@Autowired
	private BotGTemplateMessageRepo botGTemplateMessageRepo;

	@Autowired
	private BotTemplateElementRepo botTemplateElementRepo;

	@Autowired
	private BotWebserviceMessageRepo botWebserviceMessageRepo;

	@Autowired
	private BotWebserviceMappingRepo botWebserviceMappingRepo;

	public List<BotInteractionMessage> findInteractionMessagesByInteractionId(Integer interactionId) {
		return interactionMessageRepo.findByBotInteractionInteractionIdOrderByMessagePriority(interactionId);
	}

	public BotQuickReplyMessage findQuickReplyMessageByMessageId(Integer messageId) {
		return botQuickReplyMessageRepo.findByBotInteractionMessageMessageId(messageId);
	}

	public List<BotButton> findButtonsByQuickReplyMessageId(Integer quickMsgId) {
		return botButtonRepo.findByBotQuickReplyMessageQuickMsgId(quickMsgId);
	}

	public BotInteraction findInteractionByPayload(String payload) {
		return botInteractionRepo.findByPayload(payload);
	}

	public BotTextMessage findTextMessageByMessageId(Integer messageId) {
		return botTextMessageRepo.findByBotInteractionMessageMessageId(messageId);
	}

	public BotGTemplateMessage findGTemplateMessageByMessageId(Integer messageId) {
		return botGTemplateMessageRepo.findByBotInteractionMessageMessageId(messageId);
	}

	@Override
	public List<BotTemplateElement> findTemplateElementsByGTMsgId(Integer gTMsgId) {

		return botTemplateElementRepo.findByBotGTemplateMessageGTMsgId(gTMsgId);
	}

	@Override
	public List<BotButton> findButtonsByTemplateElementId(Integer elementId) {
		// TODO Auto-generated method stub
		return botButtonRepo.findByBotTemplateElementElementId(elementId);
	}

	@Override
	public BotWebserviceMessage findWebserviceMessageByMessageId(Integer messageId) {
		// TODO Auto-generated method stub
		return botWebserviceMessageRepo.findByBotInteractionMessageMessageId(messageId);
	}

	@Override
	public List<BotWebserviceMapping> findWebserviceMappingByWsId(Integer wsId) {
		// TODO Auto-generated method stub
		return botWebserviceMappingRepo.findByBotWebserviceMessageWsMsgId(wsId);
	}
}
