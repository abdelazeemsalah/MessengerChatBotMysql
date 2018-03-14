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
 * The persistent class for the BOT_BUTTONS database table.
 */
@Entity
@Table(name = "bot_buttons")
@NamedQuery(name = "BotButton.findAll", query = "SELECT b FROM BotButton b")
public class BotButton implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BUTTON_ID")
	private int buttonId;

	@Column(name = "BUTTON_PAYLOAD")
	private String buttonPayload;

	@Column(name = "BUTTON_TYPE")
	private int buttonType;

	@Column(name = "BUTTON_URL")
	private String buttonUrl;

	@Column(name = "IS_STATIC")
	private byte isStatic;

	// bi-directional many-to-one association to BotText
	@ManyToOne
	@JoinColumn(name = "TEXT_ID")
	private BotText botText;

	// bi-directional many-to-one association to BotQuickReplyMessage
	@ManyToOne
	@JoinColumn(name = "QUICK_MSG_ID")
	private BotQuickReplyMessage botQuickReplyMessage;

	@ManyToOne
	@JoinColumn(name = "ELEMENT_ID")
	private BotTemplateElement botTemplateElement;

	@Column(name = "BUTTON_IMAGE_URL")
	private String buttonImageUrl;

	public BotButton() {
	}

	public int getButtonId() {
		return this.buttonId;
	}

	public void setButtonId(int buttonId) {
		this.buttonId = buttonId;
	}

	public String getButtonPayload() {
		return this.buttonPayload;
	}

	public void setButtonPayload(String buttonPayload) {
		this.buttonPayload = buttonPayload;
	}

	public int getButtonType() {
		return this.buttonType;
	}

	public void setButtonType(int buttonType) {
		this.buttonType = buttonType;
	}

	public String getButtonUrl() {
		return this.buttonUrl;
	}

	public void setButtonUrl(String buttonUrl) {
		this.buttonUrl = buttonUrl;
	}

	public byte getIsStatic() {
		return this.isStatic;
	}

	public void setIsStatic(byte isStatic) {
		this.isStatic = isStatic;
	}

	public BotText getBotText() {
		return this.botText;
	}

	public void setBotText(BotText botText) {
		this.botText = botText;
	}

	public BotQuickReplyMessage getBotQuickReplyMessage() {
		return this.botQuickReplyMessage;
	}

	public void setBotQuickReplyMessage(BotQuickReplyMessage botQuickReplyMessage) {
		this.botQuickReplyMessage = botQuickReplyMessage;
	}

	public BotTemplateElement getBotTemplateElement() {
		return botTemplateElement;
	}

	public void setBotTemplateElement(BotTemplateElement botTemplateElement) {
		this.botTemplateElement = botTemplateElement;
	}

	public String getButtonImageUrl() {
		return buttonImageUrl;
	}

	public void setButtonImageUrl(String buttonImageUrl) {
		this.buttonImageUrl = buttonImageUrl;
	}

}