package com.chatbot.controller;

import static java.util.Optional.empty;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.chatbot.entity.BotButton;
import com.chatbot.entity.BotGTemplateMessage;
import com.chatbot.entity.BotInteraction;
import com.chatbot.entity.BotInteractionMessage;
import com.chatbot.entity.BotQuickReplyMessage;
import com.chatbot.entity.BotTemplateElement;
import com.chatbot.entity.BotTextMessage;
import com.chatbot.entity.BotWebserviceMapping;
import com.chatbot.entity.BotWebserviceMessage;
import com.chatbot.services.ChatBotService;
import com.chatbot.util.Utils;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.message.TemplateMessage;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import com.github.messenger4j.send.message.template.GenericTemplate;
import com.github.messenger4j.send.message.template.Template;
import com.github.messenger4j.send.message.template.button.Button;
import com.github.messenger4j.send.message.template.button.PostbackButton;
import com.github.messenger4j.send.message.template.button.UrlButton;
import com.github.messenger4j.send.message.template.common.Element;
import com.github.messenger4j.webhook.event.AccountLinkingEvent;
import com.github.messenger4j.webhook.event.PostbackEvent;
import com.github.messenger4j.webhook.event.QuickReplyMessageEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;

@RestController
@RequestMapping("/callback")
public class ChatBotController {
	@Autowired
	private final Messenger messenger;

	@Autowired
	private ChatBotService chatBotService;

	private static final Logger logger = LoggerFactory.getLogger(ChatBotController.class);

	@Autowired
	public ChatBotController(final Messenger sendClient) {
		this.messenger = sendClient;

	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") final String mode,
			@RequestParam("hub.verify_token") final String verifyToken, @RequestParam("hub.challenge") final String challenge) {

		logger.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyToken, challenge);
		try {
			this.messenger.verifyWebhook(mode, verifyToken);
			return ResponseEntity.status(HttpStatus.OK).body(challenge);
		} catch (MessengerVerificationException e) {
			logger.warn("Webhook verification failed: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader("X-Hub-Signature") final String signature) {

		logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);
		try {
			// this.receiveClient.processCallbackPayload(payload, signature);
			messenger.onReceiveEvents(payload, Optional.of(signature), event -> {

				final String senderId = event.senderId();
				final java.time.Instant timestamp = event.timestamp();

				if (event.isTextMessageEvent()) {
					final TextMessageEvent textMessageEvent = event.asTextMessageEvent();
					final String messageId = textMessageEvent.messageId();
					String text = textMessageEvent.text();

					logger.debug("Received text message from '{}' at '{}' with content: {} (mid: {})", senderId, timestamp, text, messageId);
					String textToSend = "";
					// boolean isArabicMsg = isProbablyArabic(text);
					// if (isArabicMsg) {
					// text = translateToEn(text);
					// }
					textToSend = "Hey there! how can I help you?";

					// sendQuickReplyMessage(textToSend, messenger, senderId);
					// sendTextMessage(text, messenger, senderId);
					handlePayload(text, messenger, senderId);
				} else if (event.isQuickReplyMessageEvent()) {
					final QuickReplyMessageEvent quickReplyMessageEvent = event.asQuickReplyMessageEvent();
					// final String messageId = quickReplyMessageEvent.messageId();
					// String text = quickReplyMessageEvent.payload();
					// QuickReply qR1 = TextQuickReply.create("brilliant1", "BRILLIANT_PAYLOAD1");
					// QuickReply qR2 = TextQuickReply.create("brilliant2", "BRILLIANT_PAYLOAD2");
					// QuickReply qR3 = TextQuickReply.create("brilliant3", "BRILLIANT_PAYLOAD3");
					//
					// List<QuickReply> quickReplies = new ArrayList<>();
					// quickReplies.add(qR1);
					// quickReplies.add(qR2);
					// quickReplies.add(qR3);
					// sendQuickReplyMessage(quickReplies, text, messenger, senderId);

					// sendTextMessage(text, messenger, senderId);
					handlePayload(quickReplyMessageEvent.payload(), messenger, senderId);

				} else if (event.isPostbackEvent()) {
					PostbackEvent postbackEvent = event.asPostbackEvent();
					handlePayload(postbackEvent.payload().get(), messenger, senderId);

				} else if (event.isAccountLinkingEvent()) {
					AccountLinkingEvent accountLinkingEvent = event.asAccountLinkingEvent();

					if (accountLinkingEvent.status().equals(AccountLinkingEvent.Status.LINKED)) {
						String authorizationCode = accountLinkingEvent.authorizationCode().get();
					} else {

					}
					// sendQuickReplyMessage(authorizationCode, messenger, senderId);
				}

			});
			logger.debug("Processed callback payload successfully");
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (MessengerVerificationException e) {
			logger.warn("Processing of callback payload failed: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	private void sendTextMessage(String text, Messenger messenger, String senderId) {

		final String recipientId = senderId;
		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, TextMessage.create(text));

		try {
			messenger.send(payload);
		} catch (MessengerApiException | MessengerIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void handlePayload(String payload, Messenger messenger, String senderId) {
		try {
			String text;
			String payloadWithoutPrefix = "";
			if (payload.startsWith("SUB__")) {
				payload = "SUB__";
				payloadWithoutPrefix = payload.substring(5);
			}
			BotInteraction botInteraction = chatBotService.findInteractionByPayload(payload);
			if (!botInteraction.getIsSecure()) {
				List<BotInteractionMessage> interactionMessageList = chatBotService
						.findInteractionMessagesByInteractionId(botInteraction.getInteractionId());
				ArrayList<MessagePayload> messagePayloadList = new ArrayList<>();
				MessagePayload messagePayload = null;
				for (BotInteractionMessage botInteractionMessage : interactionMessageList) {

					Integer messageTypeId = botInteractionMessage.getBotMessageType().getMessageTypeId();
					Integer messageId = botInteractionMessage.getMessageId();

					if (botInteractionMessage.isStatic()) {
						// text message
						if (messageTypeId == MessageTypeEnum.TEXTMESSAGE.getValue()) {
							BotTextMessage botTextMessage = chatBotService.findTextMessageByMessageId(messageId);
							text = botTextMessage.getBotText().getEnglishText();
							messagePayload = MessagePayload.create(senderId, MessagingType.RESPONSE, TextMessage.create(text));
							messagePayloadList.add(messagePayload);
							// sendTextMessage(text, messenger, senderId);
						}
						// quick reply
						else if (messageTypeId == MessageTypeEnum.QUICKREPLYMESSAGE.getValue()) {
							BotQuickReplyMessage botQuickReplyMessage = chatBotService.findQuickReplyMessageByMessageId(messageId);
							text = botQuickReplyMessage.getBotText().getEnglishText();
							List<QuickReply> quickReplies = new ArrayList<>();
							List<BotButton> quickReplyButtonList = chatBotService
									.findButtonsByQuickReplyMessageId(botQuickReplyMessage.getQuickMsgId());
							QuickReply quickReply = null;
							for (BotButton botButton : quickReplyButtonList) {
								/*
								 * // Postback if (botButton.getButtonType() == 1) { quickReply =
								 * TextQuickReply.create(botButton.getBotText().getEnglishText(),
								 * botButton.getButtonPayload()); } // URL else if (botButton.getButtonType() == 2) {
								 * quickReply = TextQuickReply.create(botButton.getBotText().getEnglishText(),
								 * botButton.getButtonPayload(), Optional.of(new URL(botButton.getButtonUrl()))); }
								 */
								if (botButton.getButtonImageUrl() != null)
									quickReply = TextQuickReply.create(botButton.getBotText().getEnglishText(), botButton.getButtonPayload(),
											Optional.of(new URL(botButton.getButtonImageUrl())));
								else
									quickReply = TextQuickReply.create(botButton.getBotText().getEnglishText(), botButton.getButtonPayload());

								quickReplies.add(quickReply);
							}
							// QuickReply qR1 = TextQuickReply.create("brilliant1", "BRILLIANT_PAYLOAD1");
							// QuickReply qR2 = TextQuickReply.create("brilliant2", "BRILLIANT_PAYLOAD2");
							// QuickReply qR3 = TextQuickReply.create("brilliant3", "BRILLIANT_PAYLOAD3");

							// quickReplies.add(qR1);
							// quickReplies.add(qR2);
							// quickReplies.add(qR3);
							// sendQuickReplyMessage(quickReplies, text, messenger, senderId);
							Optional<List<QuickReply>> quickRepliesOp = Optional.of(quickReplies);
							messagePayload = MessagePayload.create(senderId, MessagingType.RESPONSE,
									TextMessage.create(text, quickRepliesOp, empty()));
							messagePayloadList.add(messagePayload);
						}
						// generic template
						else if (messageTypeId == MessageTypeEnum.GENERICTEMPLATEMESSAGE.getValue()) {
							BotGTemplateMessage botGTemplateMessage = chatBotService.findGTemplateMessageByMessageId(messageId);
							List<BotTemplateElement> botTemplateElementList = chatBotService
									.findTemplateElementsByGTMsgId(botGTemplateMessage.getGTMsgId());
							List<Element> elements = new ArrayList<>();
							for (BotTemplateElement botTemplateElement : botTemplateElementList) {
								List<BotButton> elementButtonList = chatBotService.findButtonsByTemplateElementId(botTemplateElement.getElementId());
								List<Button> buttonsList = new ArrayList<>();
								for (BotButton botButton : elementButtonList) {
									Button button = null;
									// PostBack
									if (botButton.getButtonType() == ButtonTypeEnum.POSTBACK.getValue()) {
										button = PostbackButton.create(botButton.getBotText().getEnglishText(), botButton.getButtonPayload());
									}
									// URL
									else if (botButton.getButtonType() == ButtonTypeEnum.URL.getValue()) {
										button = UrlButton.create(botButton.getBotText().getEnglishText(), new URL(botButton.getButtonUrl()));
									}
									buttonsList.add(button);
								}
								Element element = Element.create(botTemplateElement.getTitle().getEnglishText(),
										Optional.of(botTemplateElement.getSubTitle().getEnglishText()),
										Optional.of(new URL(botTemplateElement.getImageUrl())), empty(), Optional.of(buttonsList));

								elements.add(element);
							}
							Template template = GenericTemplate.create(elements);
							messagePayload = MessagePayload.create(senderId, MessagingType.RESPONSE, TemplateMessage.create(template));
							messagePayloadList.add(messagePayload);
							// sendTemplateMessage(elements, messenger, senderId);

						}
					}
					// Dynamic Scenario
					else {
						BotWebserviceMessage botWebserviceMessage = chatBotService.findWebserviceMessageByMessageId(messageId);
						RestTemplate restTemplate = new RestTemplate();

						// Build request headers if exists
						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(Utils.getMediaType(botWebserviceMessage.getContentType()));

						if (Utils.isNotEmpty(botWebserviceMessage.getHeaderParams())) {
							// split by comma separated to get all params then split by equal to get key and value for
							// each param

							// Map<String, String> headersMap = restRepoDTO.getRequestHeaders().stream()
							// .collect(Collectors.toMap(RestRequestHeader::getRestHeaderName,
							// RestRequestHeader::getRestHeaderValue));
							//
							// headers.setAll(headersMap);
						}

						/*
						 * // Build request body HttpEntity<String> entity = new HttpEntity<String>("", headers); // Get
						 * response ResponseEntity<String> response =
						 * restTemplate.exchange(botWebserviceMessage.getWsUrl(),
						 * Utils.getHttpMethod(botWebserviceMessage.getBotMethodType().getMethodTypeId()), entity,
						 * String.class); System.out.println(response.getBody());
						 */

						HttpEntity<String> entity = new HttpEntity<String>("", headers);
						// Get response
						ResponseEntity<String> response = restTemplate.exchange(botWebserviceMessage.getWsUrl(),
								Utils.getHttpMethod(botWebserviceMessage.getBotMethodType().getMethodTypeId()), entity, String.class);
						System.out.println(response.getBody());
						String jsonBodyString = response.getBody();
						// jsonBodyString = "{ \"id\":1,\"values\":" + jsonBodyString + "}";
						Object jsonBodyObject = new JSONTokener(jsonBodyString).nextValue();
						JSONObject jsonObject = null;
						JSONArray jsonArray = null;
						if (jsonBodyObject instanceof JSONObject) {
							jsonObject = (JSONObject) jsonBodyObject;
							jsonArray = jsonObject.getJSONArray(botWebserviceMessage.getListParamName());
						} else if (jsonBodyObject instanceof JSONArray) {
							jsonArray = (JSONArray) jsonBodyObject;
						}
						List<Element> elements = new ArrayList<>();
						List<Button> buttonsList = new ArrayList<>();
						// string
						if (botWebserviceMessage.getOutType().getInOutTypeId() == 1) {

						}
						// object
						else if (botWebserviceMessage.getOutType().getInOutTypeId() == 2) {
							String params[] = botWebserviceMessage.getOutputParams().split(",");

							for (String string : params) {
								System.out.println("Param is" + jsonObject.getString(string));
							}

						}
						// list<object>
						else if (botWebserviceMessage.getOutType().getInOutTypeId() == 3) {
							// String params[] = botWebserviceMessage.getOutputParams().split(",");
							List<BotWebserviceMapping> webServiceMappingList = chatBotService
									.findWebserviceMappingByWsId(botWebserviceMessage.getWsMsgId());
							if (messageTypeId == MessageTypeEnum.GENERICTEMPLATEMESSAGE.getValue()) {
								for (int i = 0; i < jsonArray.length(); i++) {
									buttonsList = new ArrayList<>();
									JSONObject jsonObject2 = jsonArray.getJSONObject(i);
									// for (String string : params) {
									// Object value = jsonObject2.get(string);
									// if (value instanceof String) {
									// System.out.println("Param is" + jsonObject2.get(string).toString());
									// } else if (value instanceof Integer) {
									// System.out.println("Param is" + String.valueOf(jsonObject2.get(string)));
									// }
									// else if (value instanceof Double) {
									// System.out.println("Param is" + String.valueOf(jsonObject2.get(string)));
									// }

									// System.out.println(string + " ----> " + String.valueOf(value));
									// }
									String title = null;
									String subTitle = null;
									String payload2 = null;
									String buttonText = null;
									for (BotWebserviceMapping botWebserviceMapping : webServiceMappingList) {
										// output params
										if (botWebserviceMapping.getFieldType() == 2) {
											Object valueObject = jsonObject2.get(botWebserviceMapping.getFieldName());
											String value = String.valueOf(valueObject);
											// if (messageTypeId == MessageTypeEnum.GENERICTEMPLATEMESSAGE.getValue()) {

											if (botWebserviceMapping.getFieldMapedTo().equals("title"))
												title = value;
											else if (botWebserviceMapping.getFieldMapedTo().equals("subTitle"))
												subTitle = value;
											else if (botWebserviceMapping.getFieldMapedTo().equals("payload"))
												payload2 = value;
											else if (botWebserviceMapping.getFieldMapedTo().equals("buttonText"))
												buttonText = value;

											// }
										}
									}

									Button button = null;
									if (botWebserviceMessage.isStatic())
										buttonText = botWebserviceMessage.getButtonText().getEnglishText();
									button = PostbackButton.create(buttonText, payload2);

									buttonsList.add(button);

									Element element = Element.create(title, Optional.of(subTitle), empty(), empty(), Optional.of(buttonsList));
									if (elements.size() < 10)
										elements.add(element);

								}
								Template template = GenericTemplate.create(elements);
								messagePayload = MessagePayload.create(senderId, MessagingType.RESPONSE, TemplateMessage.create(template));
								messagePayloadList.add(messagePayload);
							}
						}
					}
				}
				sendMltipleMessages(messagePayloadList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void sendMltipleMessages(ArrayList<MessagePayload> responses) {

		for (MessagePayload response : responses) {
			try {
				messenger.send(response);
			} catch (MessengerApiException | MessengerIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void sendQuickReplyMessage(List<QuickReply> quickReplies, String text, Messenger messenger, String senderId) {

		final String recipientId = senderId;

		Optional<List<QuickReply>> quickRepliesOp = Optional.of(quickReplies);
		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, TextMessage.create(text, quickRepliesOp, empty()));

		try {
			messenger.send(payload);
		} catch (MessengerApiException | MessengerIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendTemplateMessage(List<Element> elements, Messenger messenger, String recipientId) {

		// List<Button> buttons=new ArrayList<>();
		// buttons.add(e)
		/*
		 * URL imageUrl = null; try { imageUrl = new URL("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
		 * } catch (MalformedURLException e1) { // TODO Auto-generated catch block e1.printStackTrace(); } Button
		 * button11 = PostbackButton.create("button1", "1"); Button button12 = PostbackButton.create("button2", "1");
		 * List<Button> buttons1 = new ArrayList<>(); buttons1.add(button11); buttons1.add(button12);
		 * Optional<List<Button>> buttons1Op = Optional.of(buttons1); Button button21 = PostbackButton.create("button1",
		 * "button1 in element2"); Button button22 = PostbackButton.create("button2", "button2 in element2");
		 * List<Button> buttons2 = new ArrayList<>(); buttons2.add(button21); buttons2.add(button22);
		 * Optional<List<Button>> buttons2Op = Optional.of(buttons2); URL buttonURL = null; try { buttonURL = new
		 * URL("https://my.etisalat.eg"); } catch (MalformedURLException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } Button button31 = UrlButton.create("button1", buttonURL); Button button32 =
		 * PostbackButton.create("button2", "button2 in element3"); List<Button> buttons3 = new ArrayList<>();
		 * buttons3.add(button31); buttons3.add(button32); Optional<List<Button>> buttons3Op = Optional.of(buttons3);
		 * Optional<URL> imageUrlOp = Optional.of(imageUrl); Element element1 = Element.create("Title1",
		 * Optional.of("subtitle1"), imageUrlOp, empty(), buttons1Op); Element element2 = Element.create("Title2",
		 * Optional.of("subtitle2"), empty(), empty(), buttons2Op); Element element3 = Element.create("Title3",
		 * Optional.of("subtitle3"), empty(), empty(), buttons3Op); List<Element> elements = new ArrayList<>();
		 * elements.add(element1); elements.add(element2); elements.add(element3);
		 */
		Template template = GenericTemplate.create(elements);
		final MessagePayload payload = MessagePayload.create(recipientId, MessagingType.RESPONSE, TemplateMessage.create(template));

		try {
			messenger.send(payload);
		} catch (MessengerApiException | MessengerIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/{param}", method = RequestMethod.GET)
	public ResponseEntity<String> getMsg(@PathVariable("param") String msg) {
		try {
			String output = "Jersey say : " + msg;

			BotWebserviceMessage botWebserviceMessage = chatBotService.findWebserviceMessageByMessageId(6);
			RestTemplate restTemplate = new RestTemplate();

			// Build request headers if exists
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(Utils.getMediaType(botWebserviceMessage.getContentType()));

			if (Utils.isNotEmpty(botWebserviceMessage.getHeaderParams())) {
				// split by comma separated to get all params then split by equal to get key and value for
				// each param

				// Map<String, String> headersMap = restRepoDTO.getRequestHeaders().stream()
				// .collect(Collectors.toMap(RestRequestHeader::getRestHeaderName,
				// RestRequestHeader::getRestHeaderValue));
				//
				// headers.setAll(headersMap);
			}

			// Build request body
			HttpEntity<String> entity = new HttpEntity<String>("", headers);

			// Get response
			ResponseEntity<String> response = restTemplate.exchange(botWebserviceMessage.getWsUrl(),
					Utils.getHttpMethod(botWebserviceMessage.getBotMethodType().getMethodTypeId()), entity, String.class);
			System.out.println(response.getBody());
			String jsonBodyString = response.getBody();
			// jsonBodyString = "{ \"id\":1,\"values\":" + jsonBodyString + "}";
			Object jsonBodyObject = new JSONTokener(jsonBodyString).nextValue();
			JSONObject jsonObject = null;
			JSONArray jsonArray = null;
			if (jsonBodyObject instanceof JSONObject) {
				jsonObject = (JSONObject) jsonBodyObject;
				jsonArray = jsonObject.getJSONArray(botWebserviceMessage.getListParamName());
			} else if (jsonBodyObject instanceof JSONArray) {
				jsonArray = (JSONArray) jsonBodyObject;
			}
			List<Element> elements = new ArrayList<>();
			List<Button> buttonsList = new ArrayList<>();
			if (botWebserviceMessage.getOutType().getInOutTypeId() == 2) {
				String params[] = botWebserviceMessage.getOutputParams().split(",");

				for (String string : params) {
					System.out.println("Param is" + jsonObject.getString(string));
				}

			} else if (botWebserviceMessage.getOutType().getInOutTypeId() == 3) {
				// String params[] = botWebserviceMessage.getOutputParams().split(",");
				List<BotWebserviceMapping> webServiceMappingList = chatBotService.findWebserviceMappingByWsId(botWebserviceMessage.getWsMsgId());

				for (int i = 0; i < jsonArray.length(); i++) {
					buttonsList = new ArrayList<>();
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					// for (String string : params) {
					// Object value = jsonObject2.get(string);
					// if (value instanceof String) {
					// System.out.println("Param is" + jsonObject2.get(string).toString());
					// } else if (value instanceof Integer) {
					// System.out.println("Param is" + String.valueOf(jsonObject2.get(string)));
					// }
					// else if (value instanceof Double) {
					// System.out.println("Param is" + String.valueOf(jsonObject2.get(string)));
					// }

					// System.out.println(string + " ----> " + String.valueOf(value));
					// }
					String title = null;
					String subTitle = null;
					String payload = null;
					for (BotWebserviceMapping botWebserviceMapping : webServiceMappingList) {
						Object valueObject = jsonObject2.get(botWebserviceMapping.getFieldName());
						String value = String.valueOf(valueObject);
						if (3 == MessageTypeEnum.GENERICTEMPLATEMESSAGE.getValue()) {

							if (botWebserviceMapping.getFieldMapedTo().equals("title"))
								title = value;
							else if (botWebserviceMapping.getFieldMapedTo().equals("subTitle"))
								subTitle = value;
							else if (botWebserviceMapping.getFieldMapedTo().equals("payload"))
								payload = value;

						}
					}
					Button button = PostbackButton.create("Subscribe", payload);
					buttonsList.add(button);

					Element element = Element.create(title, Optional.of(subTitle), empty(), empty(), Optional.of(buttonsList));

					elements.add(element);

				}
				Template template = GenericTemplate.create(elements);
				// messagePayload = MessagePayload.create(senderId, MessagingType.RESPONSE,
				// TemplateMessage.create(template));
				// messagePayloadList.add(messagePayload);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// List<BotInteractionMessage> interactionMessageList =
		// chatBotService.findInteractionMessagesByInteractionId(0);
		// if (interactionMessageList != null && interactionMessageList.size() > 0)
		// System.out.println(interactionMessageList.get(0).getMessagePriority());
		return ResponseEntity.status(200).body("Hello");

	}

	public enum ButtonTypeEnum {
		POSTBACK(1), URL(2);
		private final int buttonTypeId;

		private ButtonTypeEnum(int typeId) {
			this.buttonTypeId = typeId;
		}

		public int getValue() {
			return buttonTypeId;
		}
	}

	public enum MessageTypeEnum {
		TEXTMESSAGE(1), QUICKREPLYMESSAGE(2), GENERICTEMPLATEMESSAGE(3);
		private final int messageTypeId;

		private MessageTypeEnum(int messageTypeId) {
			this.messageTypeId = messageTypeId;
		}

		public int getValue() {
			return messageTypeId;
		}
	}

	/*
	 * public enum ContentTypeEnum { APPLICATION_JSON(1), APPLICATION_XML(2); private final int contentTypeId; private
	 * ContentTypeEnum(int contentTypeId) { this.contentTypeId = contentTypeId; } public int getValue() { return
	 * contentTypeId; } }
	 */

	/*
	 * public enum MethodTypeEnum { GET(1), POST(2); private final int methodtTypeId; private MethodTypeEnum(int
	 * methodtTypeId) { this.methodtTypeId = methodtTypeId; } public int getValue() { return methodtTypeId; } }
	 */

	// to be added in util CLass

}
