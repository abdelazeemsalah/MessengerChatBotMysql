package com.chatbot.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * The persistent class for the BOT_INTERACTIONS database table.
 */
@Entity
@Table(name = "bot_interactions")
@NamedQuery(name = "BotInteraction.findAll", query = "SELECT b FROM BotInteraction b")
public class BotInteraction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INTERACTION_ID")
	private int interactionId;

	@Column(name = "INTERACTION_NAME")
	private String interactionName;

	@Column(name = "PAYLOAD")
	private String payload;

	@Column(name = "IS_SECURE")
	@Type(type = "numeric_boolean")
	private boolean isSecure;

	// bi-directional many-to-one association to BotInteractionMessage
	@OneToMany(mappedBy = "botInteraction")
	private List<BotInteractionMessage> botInteractionMessages;

	public BotInteraction() {
	}

	public int getInteractionId() {
		return this.interactionId;
	}

	public void setInteractionId(int interactionId) {
		this.interactionId = interactionId;
	}

	public String getInteractionName() {
		return this.interactionName;
	}

	public void setInteractionName(String interactionName) {
		this.interactionName = interactionName;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public boolean getIsSecure() {
		return this.isSecure;
	}

	public void setIsSecure(boolean isSecure) {
		this.isSecure = isSecure;
	}

	public List<BotInteractionMessage> getBotInteractionMessages() {
		return this.botInteractionMessages;
	}

	public void setBotInteractionMessages(List<BotInteractionMessage> botInteractionMessages) {
		this.botInteractionMessages = botInteractionMessages;
	}

	public BotInteractionMessage addBotInteractionMessage(BotInteractionMessage botInteractionMessage) {
		getBotInteractionMessages().add(botInteractionMessage);
		botInteractionMessage.setBotInteraction(this);

		return botInteractionMessage;
	}

	public BotInteractionMessage removeBotInteractionMessage(BotInteractionMessage botInteractionMessage) {
		getBotInteractionMessages().remove(botInteractionMessage);
		botInteractionMessage.setBotInteraction(null);

		return botInteractionMessage;
	}

}