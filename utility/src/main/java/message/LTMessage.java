package main.java.message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

public abstract class LTMessage {

	protected static final String TYPE = "type";
	protected Map<String, String> messageData;

	public LTMessage(String type) {
		messageData = new HashMap<String, String>();
		messageData.put(TYPE, type);
	}

	/**
	 * Returns the message as a string.
	 * 
	 * @return message string
	 */
	public String getMessage() {
		JSONObject message = new JSONObject();
		Iterator<String> keys = messageData.keySet().iterator();
		String key = "";

		while (keys.hasNext()) {
			key = keys.next();
			message.put(key, messageData.get(key));
		}

		return message.toString();
	}

	/**
	 * Returns the type of the message.
	 * 
	 * @return message type
	 */
	public String getType() {
		return messageData.get(TYPE);
	}

	/**
	 * Returns the message as a string by calling the getMessage method.
	 */
	@Override
	public String toString() {
		return getMessage();
	}
}
