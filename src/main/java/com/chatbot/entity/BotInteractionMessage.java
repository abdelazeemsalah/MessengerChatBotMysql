package com.chatbot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the BOT_INTERACTION_MESSAGES database table.
 */
@Entity
@Table(name = "bot_interaction_messages")
@NamedQuery(name = "BotInteractionMessage.findAll", query = "SELECT b FROM BotInteractionMessage b")
public class BotInteractionMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MESSAGE_ID")
	private int messageId;

	@Column(name = "MESSAGE_PRIORITY")
	private int messagePriority;

	// bi-directional many-to-one association to BotMessageType
	@ManyToOne
	@JoinColumn(name = "MESSAGE_TYPE")
	private BotMessageType botMessageType;

	// bi-directional many-to-one association to BotInteraction
	@ManyToOne
	@JoinColumn(name = "INTERACTION_ID")
	private BotInteraction botInteraction;

	public BotInteractionMessage() {
	}

	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getMessagePriority() {
		return this.messagePriority;
	}

	public void setMessagePriority(int messagePriority) {
		this.messagePriority = messagePriority;
	}

	public BotMessageType getBotMessageType() {
		return this.botMessageType;
	}

	public void setBotMessageType(BotMessageType botMessageType) {
		this.botMessageType = botMessageType;
	}

	public BotInteraction getBotInteraction() {
		return this.botInteraction;
	}

	public void setBotInteraction(BotInteraction botInteraction) {
		this.botInteraction = botInteraction;
	}

}