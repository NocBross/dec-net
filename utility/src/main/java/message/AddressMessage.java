package main.java.message;

import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONTokener;

import main.java.validator.IPAddressValidator;
import main.java.validator.PortValidator;
import main.java.validator.UserIDValidator;
import main.java.validator.Validator;

public class AddressMessage extends Message {

    public static final String ID = "ip_address";
    private static final String LOCAL_ADDRESS = "local_address";
    private static final String LOCAL_PORT = "local_port";
    private static final String EXTERNAL_ADDRESS = "external_address";
    private static final String EXTERNAL_PORT = "external_port";
    private static final String USER_ID = "userID";

    private Validator<String> ipAddressValidator;
    private Validator<String> userIDValidator;
    private Validator<Integer> portValidator;

    public AddressMessage() {
        super(ID);
        ipAddressValidator = new IPAddressValidator();
        userIDValidator = new UserIDValidator();
        portValidator = new PortValidator();
    }

    /**
     * Creates a AddressMessage from a given string.
     * 
     * @param messageString
     *            - string which will be converted
     * @return AddressMessage of the string or null if the string not representing a
     *         AddressMessage
     */
    public static AddressMessage parse(String messageString) {
        if (messageString != null) {
            try {
                JSONTokener parser = new JSONTokener(messageString);
                JSONObject jsonMessage = (JSONObject) parser.nextValue();

                if (jsonMessage.getString(TYPE).equals(ID)) {
                    AddressMessage message = new AddressMessage();

                    Iterator<String> iterator = jsonMessage.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        switch (key) {
                            case LOCAL_ADDRESS:
                                message.setLocalAddress(jsonMessage.getString(LOCAL_ADDRESS));
                                break;
                            case LOCAL_PORT:
                                message.setLocalPort(jsonMessage.getInt(LOCAL_PORT));
                                break;
                            case EXTERNAL_ADDRESS:
                                message.setExternalAddress(jsonMessage.getString(EXTERNAL_ADDRESS));
                                break;
                            case EXTERNAL_PORT:
                                message.setExternalPort(jsonMessage.getInt(EXTERNAL_PORT));
                                break;
                            case USER_ID:
                                message.setUserID(jsonMessage.getString(USER_ID));
                                break;
                            default:
                                break;
                        }
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

    public int getLocalPort() {
        try {
            return Integer.valueOf(messageData.get(LOCAL_PORT));
        } catch (Exception e) {
            return -1;
        }
    }

    public int getExternalPort() {
        try {
            return Integer.valueOf(messageData.get(EXTERNAL_PORT));
        } catch (Exception e) {
            return -1;
        }
    }

    public String getLocalAddress() {
        try {
            return messageData.get(LOCAL_ADDRESS);
        } catch (Exception e) {
            return null;
        }
    }

    public String getExternalAddress() {
        try {
            return messageData.get(EXTERNAL_ADDRESS);
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserID() {
        try {
            return messageData.get(USER_ID);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean setLocalPort(int localPort) {
        if (portValidator.validate(localPort)) {
            messageData.put(LOCAL_PORT, String.valueOf(localPort));
            return true;
        } else {
            return false;
        }
    }

    public boolean setExternalPort(int externalPort) {
        if (portValidator.validate(externalPort)) {
            messageData.put(EXTERNAL_PORT, String.valueOf(externalPort));
            return true;
        } else {
            return false;
        }
    }

    public boolean setLocalAddress(String localAddress) {
        if (ipAddressValidator.validate(localAddress)) {
            messageData.put(LOCAL_ADDRESS, localAddress);
            return true;
        } else {
            return false;
        }
    }

    public boolean setExternalAddress(String externalAddress) {
        if (ipAddressValidator.validate(externalAddress)) {
            messageData.put(EXTERNAL_ADDRESS, externalAddress);
            return true;
        } else {
            return false;
        }
    }

    public boolean setUserID(String userID) {
        if (userIDValidator.validate(userID)) {
            messageData.put(USER_ID, userID);
            return true;
        } else {
            return false;
        }
    }

}
