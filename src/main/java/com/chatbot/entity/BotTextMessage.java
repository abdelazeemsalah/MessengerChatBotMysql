package com.chatbot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * The persistent class for the BOT_TEXT_MESSAGES database table.
 */
@Entity
@Table(name = "bot_text_messages")
@NamedQuery(name = "BotTextMessage.findAll", query = "SELECT b FROM BotTextMessage b")
public class BotTextMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TEXT_MSG_ID")
	private int textMsgId;

	@Column(name = "IS_STATIC")
	@Type(type = "numeric_boolean")
	private boolean isStatic;

	// bi-directional many-to-one association to BotText
	@ManyToOne
	@JoinColumn(name = "TEXT_ID")
	private BotText botText;

	@ManyToOne
	@JoinColumn(name = "MESSAGE_ID")
	private BotInteractionMessage botInteractionMessage;

	public BotTextMessage() {
	}

	public int getTextMsgId() {
		return this.textMsgId;
	}

	public void setTextMsgId(int textMsgId) {
		this.textMsgId = textMsgId;
	}

	public boolean getIsStatic() {
		return this.isStatic;
	}

	public void setIsStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public BotText getBotText() {
		return this.botText;
	}

	public void setBotText(BotText botText) {
		this.botText = botText;
	}

	public BotInteractionMessage getBotInteractionMessage() {
		return botInteractionMessage;
	}

	public void setBotInteractionMessage(BotInteractionMessage botInteractionMessage) {
		this.botInteractionMessage = botInteractionMessage;
	}

}