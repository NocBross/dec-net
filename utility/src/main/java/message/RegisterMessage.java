package main.java.message;

import org.json.JSONObject;
import org.json.JSONTokener;

import main.java.validator.PasswordValidator;
import main.java.validator.Validator;

/**
 * This class represents the register message which is used by the client to
 * register a new user at the server.
 * 
 * @author Kay Oliver Szesny
 *
 */

public class RegisterMessage extends LTMessage {

	public static final String ID = "register";
	private static final String Nickname = "nickname";
	private static final String PASSWORD = "password";
	private Validator passwordValidator;

	public RegisterMessage() {
		super(ID);

		passwordValidator = new PasswordValidator();
	}

	/**
	 * Creates a RegisterMessage from a given string.
	 * 
	 * @param messageString
	 *            - string which will be converted
	 * @return RegisterMessage of the string or null if the string not representing
	 *         a RegisterMessage
	 */
	public static RegisterMessage parse(String messageString) {
		if (messageString != null) {
			try {
				JSONTokener parser = new JSONTokener(messageString);
				JSONObject jsonMessage = (JSONObject) parser.nextValue();

				if (jsonMessage.getString(TYPE).equals(ID)) {
					RegisterMessage message = new RegisterMessage();

					message.setNickname(jsonMessage.getString(Nickname));
					message.setPassword(jsonMessage.getString(PASSWORD));

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
	 * Returns the mail address.
	 * 
	 * @return mail address
	 */
	public String getNickname() {
		return messageData.get(Nickname);
	}

	/**
	 * Returns the password.
	 * 
	 * @return password
	 */
	public String getPassword() {
		return messageData.get(PASSWORD);
	}

	/**
	 * Sets the email property of the message.
	 * 
	 * @param mailAddress
	 *            - email address of the user
	 * @return true in case the given mail address is a correct mail address and it
	 *         was added; false otherwise
	 */
	public boolean setNickname(String mailAddress) {
		return (messageData.putIfAbsent(Nickname, mailAddress) == null);
	}

	/**
	 * Sets the password property of the message.
	 * 
	 * @param password
	 *            - password of the user
	 * @return true in case of the password is a correct password and it was added;
	 *         false otherwise
	 */
	public boolean setPassword(String password) {
		if (passwordValidator.validate(password)) {
			return (messageData.putIfAbsent(PASSWORD, password) == null);
		} else {
			return false;
		}
	}
}
