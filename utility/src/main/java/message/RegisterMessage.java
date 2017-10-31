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

public class RegisterMessage extends Message {

    public static final String ID = "register";
    private static final String USER_ID = "userID";
    private static final String PASSWORD = "password";
    private Validator<String> passwordValidator;

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

                    message.setUserID(jsonMessage.getString(USER_ID));
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
     * Returns the user id.
     * 
     * @return user id
     */
    public String getUserID() {
        return messageData.get(USER_ID);
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
     * Sets the user id of the message.
     * 
     * @param userID
     *            - user id of the user
     */
    public void setUserID(String userID) {
        messageData.put(USER_ID, userID);
    }

    /**
     * Sets the password of the message.
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
