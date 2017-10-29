package main.java.message;

import org.json.JSONObject;
import org.json.JSONTokener;

import main.java.validator.UserIDValidator;

public class UserIDMessage extends Message {

    public static final String ID = "userIDMessage";
    private static final String USER_ID = "userID";

    private UserIDValidator validator;

    public UserIDMessage() {
        super(ID);
        validator = new UserIDValidator();
    }

    /**
     * Creates a UserIDMessage from a given string.
     * 
     * @param messageString
     *            - string which will be converted
     * @return UserIDMessage of the string or null if the string not representing a
     *         UserIDMessage
     */
    public static UserIDMessage parse(String messageString) {
        if (messageString != null) {
            try {
                JSONTokener parser = new JSONTokener(messageString);
                JSONObject jsonMessage = (JSONObject) parser.nextValue();

                if (jsonMessage.getString(TYPE).equals(ID)) {
                    UserIDMessage message = new UserIDMessage();

                    message.setUserID(jsonMessage.getString(USER_ID));

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
     * Sets the user id of the message.
     * 
     * @param userID
     *            - user id of the user
     * @return true in case the given userID is a valid one and it was added; false
     *         otherwise
     */
    public boolean setUserID(String userID) {
        if (validator.validate(userID)) {
            messageData.put(USER_ID, userID);
            return true;
        } else {
            return false;
        }
    }

}
