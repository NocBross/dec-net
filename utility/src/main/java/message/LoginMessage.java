package main.java.message;

import org.json.JSONObject;
import org.json.JSONTokener;

import main.java.validator.PasswordValidator;
import main.java.validator.Validator;

/**
 * This class represents the login message which used by the client for server
 * login.
 * 
 * @author Kay Oliver Szesny
 *
 */

public class LoginMessage extends LTMessage {

	public static final String ID = "login";
	private static final String NICKNAME = "nickname";
	private static final String PASSWORD = "password";
	private Validator passwordValidator;

	public LoginMessage() {
		super(ID);

		passwordValidator = new PasswordValidator();
	}

	/**
	 * Creates a LoginMessage from a given string.
	 * 
	 * @param messageString
	 *            - string which will be converted
	 * @return LoginMessage of the string or null if the string not representing a
	 *         LoginMessage
	 */
	public static LoginMessage parse(String messageString) {
		if (messageString != null) {
			try {
				JSONTokener parser = new JSONTokener(messageString);
				JSONObject jsonMessage = (JSONObject) parser.nextValue();

				if (jsonMessage.getString(TYPE).equals(ID)) {
					LoginMessage message = new LoginMessage();

					message.setNickname(jsonMessage.getString(NICKNAME));
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
	 * Returns the nickname.
	 * 
	 * @return nickname
	 */
	public String getNickname() {
		return messageData.get(NICKNAME);
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
	 * @param nickname
	 *            - nickname of the user
	 * @return true in case the given mail address is a correct mail address and it
	 *         was added; false otherwise
	 */
	public boolean setNickname(String nickname) {
		return (messageData.putIfAbsent(NICKNAME, nickname) == null);
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
