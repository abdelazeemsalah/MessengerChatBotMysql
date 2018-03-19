package com.chatbot.test;

import org.json.JSONException;
import org.json.JSONObject;

public class Test {
	public static void main(String[] args) throws JSONException {
		String jsonString = "{\"userId\": 33,\"id\": 55}";
		JSONObject jsonObject = new JSONObject(jsonString);

		String[] params = { "userId", "id" };
		if (!jsonObject.isNull("userId1")) {
			Object value2 = jsonObject.get("userId1");
			System.out.println("value2 is" + value2);
		}
		for (String string : params) {
			// if(jsonObject.get(string) IsInstanceOf String)
			Object value = jsonObject.get(string);
			if (value instanceof String) {
				System.out.println("Param is" + jsonObject.get(string).toString());
			} else if (value instanceof Integer) {
				System.out.println("Param is" + String.valueOf(jsonObject.get(string)));
			}

		}
	}
}
