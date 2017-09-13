package main.java.message;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * This message is used by the client to logout from the server and tell that
 * all connected friends.
 * 
 * @author Kay Oliver Szesny
 *
 */

public class LogoutMessage extends LTMessage {

	public static final String ID = "logout";
	private static final String SENDER_NICKNAME = "sender_mail";

	public LogoutMessage() {
		super(ID);
	}

	/**
	 * Creates a LoginMessage from a given string.
	 * 
	 * @param messageString
	 *            - string which will be converted
	 * @return LoginMessage of the string or null if the string not representing a
	 *         LoginMessage
	 */
	public static LogoutMessage parse(String messageString) {
		if (messageString != null) {
			try {
				JSONTokener parser = new JSONTokener(messageString);
				JSONObject jsonMessage = (JSONObject) parser.nextValue();

				if (jsonMessage.getString(TYPE).equals(ID)) {
					LogoutMessage message = new LogoutMessage();

					message.setSender(jsonMessage.getString(LogoutMessage.SENDER_NICKNAME));

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
	 * Returns the mail address of the sender.
	 * 
	 * @return sender mail address
	 */
	public String getSender() {
		return messageData.get(SENDER_NICKNAME);
	}

	/**
	 * Sets the sender of this message if no sender is set.
	 * 
	 * @param sender
	 *            - mail address which represents the sender of this message
	 * @return true in case of success<br>
	 *         false otherwise
	 */
	public boolean setSender(String sender) {
		return (messageData.putIfAbsent(SENDER_NICKNAME, sender) == null);
	}
}
