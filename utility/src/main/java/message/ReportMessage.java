package main.java.message;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * The report message is used by the server to send the result of a request from
 * the authentication agent (e.g. login or register).
 * 
 * @author Kay Oliver Szesny
 *
 */

public class ReportMessage extends LTMessage {

	public static final String ID = "report";
	private static final String REFERENCED_MESSAGE = "refer-message";
	private static final String RESULT = "result";
	private static final String ERROR_CODE = "error-code";

	public ReportMessage() {
		super(ID);
	}

	/**
	 * Creates a ReportMessage from a given string.
	 * 
	 * @param messageString
	 *            - string which will be converted
	 * @return ReportMessage of the string or null if the string not representing a
	 *         ReportMessage
	 */
	public static ReportMessage parse(String messageString) {
		if (messageString != null) {
			try {
				JSONTokener parser = new JSONTokener(messageString);
				JSONObject jsonMessage = (JSONObject) parser.nextValue();

				if (jsonMessage.getString(TYPE).equals(ID)) {
					ReportMessage message = new ReportMessage();

					message.setReferencedMessage(jsonMessage.getString(REFERENCED_MESSAGE));
					message.setResult(jsonMessage.getBoolean(RESULT));
					if (jsonMessage.keySet().contains(ERROR_CODE)) {
						message.setErrorCode(jsonMessage.getString(ERROR_CODE));
					}

					return message;
				} else {
					return null;
				}
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Returns the message id of the message which is referenced by this report.
	 * 
	 * @return id of referenced message
	 */
	public String getReferencedMessage() {
		return messageData.get(REFERENCED_MESSAGE);
	}

	public boolean setReferencedMessage(String referencedMessage) {
		return (messageData.putIfAbsent(REFERENCED_MESSAGE, referencedMessage) == null);
	}

	public boolean getResult() {
		return Boolean.valueOf(messageData.get(RESULT));
	}

	public boolean setResult(boolean result) {
		return (messageData.putIfAbsent(RESULT, String.valueOf(result)) == null);
	}

	public String getErrorCode() {
		return messageData.get(ERROR_CODE);
	}

	public boolean setErrorCode(String errorCode) {
		return (messageData.putIfAbsent(ERROR_CODE, errorCode) == null);
	}
}
