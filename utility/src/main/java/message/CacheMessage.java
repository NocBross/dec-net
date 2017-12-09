package main.java.message;

import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONTokener;

public class CacheMessage extends Message {

    public static final String ID = "cache";
    private static final String DATA = "data";
    private static final String REQUEST_METHOD = "method";
    private static final String RESOURCE = "resource";

    public CacheMessage() {
        super(ID);
    }

    public static CacheMessage parse(String messageString) {
        if (messageString != null) {
            try {
                JSONTokener parser = new JSONTokener(messageString);
                JSONObject jsonMessage = (JSONObject) parser.nextValue();

                if (jsonMessage.getString(TYPE).equals(ID)) {
                    CacheMessage message = new CacheMessage();

                    Iterator<String> iterator = jsonMessage.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        switch (key) {
                            case DATA:
                                message.setData(jsonMessage.getString(DATA));
                                break;
                            case REQUEST_METHOD:
                                if (!message.setRequestMethod(jsonMessage.getString(REQUEST_METHOD))) {
                                    return null;
                                }
                                break;
                            case RESOURCE:
                                message.setResource(jsonMessage.getString(RESOURCE));
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

    public String getData() {
        return messageData.get(DATA);
    }

    public String getRequestMethod() {
        return messageData.get(REQUEST_METHOD);
    }

    public String getResource() {
        return messageData.get(RESOURCE);
    }

    public void setData(String data) {
        messageData.put(DATA, data);
    }

    public boolean setRequestMethod(String requestMethod) {
        if (requestMethod.equals("GET") || requestMethod.equals("POST")) {
            messageData.put(REQUEST_METHOD, requestMethod);
            return true;
        } else {
            return false;
        }
    }

    public void setResource(String resource) {
        messageData.put(RESOURCE, resource);
    }

}
