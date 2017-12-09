package main.java.message;

import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONTokener;

public class UpdateMessage extends Message {

    public static final String ID = "update";
    private static final String RESOURCE = "resource";
    private static final String USER_ID = "userID";

    public UpdateMessage() {
        super(ID);
    }

    public static UpdateMessage parse(String messageString) {
        if (messageString != null) {
            try {
                JSONTokener parser = new JSONTokener(messageString);
                JSONObject jsonMessage = (JSONObject) parser.nextValue();

                if (jsonMessage.getString(TYPE).equals(ID)) {
                    UpdateMessage message = new UpdateMessage();

                    Iterator<String> iterator = jsonMessage.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        switch (key) {
                            case RESOURCE:
                                message.setResource(jsonMessage.getString(RESOURCE));
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

    public String getResource() {
        return messageData.get(RESOURCE);
    }

    public String getUserID() {
        return messageData.get(USER_ID);
    }

    public void setResource(String resource) {
        messageData.put(RESOURCE, resource);
    }

    public void setUserID(String userID) {
        messageData.put(USER_ID, userID);
    }

}
