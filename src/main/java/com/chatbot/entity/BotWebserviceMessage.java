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
 * The persistent class for the bot_webservice_message database table.
 */
@Entity
@Table(name = "bot_webservice_message")
@NamedQuery(name = "BotWebserviceMessage.findAll", query = "SELECT b FROM BotWebserviceMessage b")
public class BotWebserviceMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "WS_MSG_ID")
	private int wsMsgId;

	@Column(name = "INPUT_PARAMS")
	private String inputParams;

	@Column(name = "OUTPUT_PARAMS")
	private String outputParams;

	@Column(name = "HEADER_PARAMS")
	private String headerParams;

	@Column(name = "WS_NAME")
	private String wsName;

	@Column(name = "WS_URL")
	private String wsUrl;

	@Column(name = "REQUEST")
	private String request;

	@Column(name = "LIST_PARAM_NAME")
	private String listParamName;

	@Column(name = "CONTENT_TYPE")
	private int contentType;

	// bi-directional many-to-one association to BotInteractionMessage
	@ManyToOne
	@JoinColumn(name = "MESSAGE_ID")
	private BotInteractionMessage botInteractionMessage;

	// bi-directional many-to-one association to BotMethodType
	@ManyToOne
	@JoinColumn(name = "METHOD_TYPE_ID")
	private BotMethodType botMethodType;

	// bi-directional many-to-one association to BotInOutType
	@ManyToOne
	@JoinColumn(name = "OUT_TYPE_ID")
	private BotInOutType outType;

	@ManyToOne
	@JoinColumn(name = "BUTTON_TEXT_ID")
	private BotText buttonText;

	@Column(name = "IS_STATIC")
	private boolean isStatic;

	public BotWebserviceMessage() {
	}

	public int getWsMsgId() {
		return this.wsMsgId;
	}

	public void setWsMsgId(int wsMsgId) {
		this.wsMsgId = wsMsgId;
	}

	public String getInputParams() {
		return this.inputParams;
	}

	public void setInputParams(String inputParams) {
		this.inputParams = inputParams;
	}

	public String getOutputParams() {
		return this.outputParams;
	}

	public void setOutputParams(String outputParams) {
		this.outputParams = outputParams;
	}

	public String getHeaderParams() {
		return this.headerParams;
	}

	public void setHeaderParams(String headerParams) {
		this.headerParams = headerParams;
	}

	public String getWsName() {
		return this.wsName;
	}

	public void setWsName(String wsName) {
		this.wsName = wsName;
	}

	public String getWsUrl() {
		return this.wsUrl;
	}

	public void setWsUrl(String wsUrl) {
		this.wsUrl = wsUrl;
	}

	public BotInteractionMessage getBotInteractionMessage() {
		return this.botInteractionMessage;
	}

	public void setBotInteractionMessage(BotInteractionMessage botInteractionMessage) {
		this.botInteractionMessage = botInteractionMessage;
	}

	public BotMethodType getBotMethodType() {
		return this.botMethodType;
	}

	public void setBotMethodType(BotMethodType botMethodType) {
		this.botMethodType = botMethodType;
	}

	public BotInOutType getOutType() {
		return outType;
	}

	public void setOutType(BotInOutType outType) {
		this.outType = outType;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public String getListParamName() {
		return listParamName;
	}

	public void setListParamName(String listParamName) {
		this.listParamName = listParamName;
	}

	public BotText getButtonText() {
		return buttonText;
	}

	public void setButtonText(BotText buttonText) {
		this.buttonText = buttonText;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

}