package com.chatbot.util;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public class Utils {
	public static boolean isNotEmpty(String obj) {
		return obj != null && obj.length() != 0;
	}

	public static HttpMethod getHttpMethod(int httpMethodId) {
		switch (httpMethodId) {
			case 1:
				return HttpMethod.GET;

			case 2:
				return HttpMethod.POST;

			default:
				return HttpMethod.GET;
		}

	}

	public static MediaType getMediaType(int mediaTypeId) {
		switch (mediaTypeId) {
			case 1:
				return MediaType.APPLICATION_JSON;
			case 2:
				return MediaType.APPLICATION_XML;

			default:
				return MediaType.APPLICATION_JSON;
		}

	}

}
