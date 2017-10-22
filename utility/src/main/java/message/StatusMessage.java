package main.java.message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

import main.java.constants.Status;

/**
 * This message is used by the client to get all friend which are currently
 * online from the server.
 * 
 * @author Kay Oliver Szesny
 *
 */

public class StatusMessage extends LTMessage {

    // attributes
    public static final String ID = "status";
    private Map<String, Status> users;
    // end of attributes


    // constructor
    public StatusMessage() {
        super(ID);
        users = new HashMap<String, Status>();
    }
    // end of constructor


    // methods
    /**
     * Creates a StatusMessage from a given string.
     * 
     * @param messageString
     *        - string which will be converted
     * @return StatusMessage of the string or null if the string not
     *         representing a StatusMessage
     */
    public static StatusMessage parse(String messageString) {
        if(messageString != null) {
            try {
                JSONTokener parser = new JSONTokener(messageString);
                JSONObject jsonMessage = (JSONObject) parser.nextValue();

                if(jsonMessage.getString(TYPE).equals(ID)) {
                    StatusMessage message = new StatusMessage();

                    Iterator<String> keys = jsonMessage.keySet().iterator();
                    while(keys.hasNext()) {
                        String key = keys.next();
                        if(key.equals(TYPE)) {
                            continue;
                        } else {
                            Status status = null;
                            switch(jsonMessage.getString(key)) {
                                case "offline":
                                    status = Status.OFFLINE;
                                    break;
                                case "online":
                                    status = Status.ONLINE;
                                    break;
                                case "busy":
                                    status = Status.BUSY;
                                    break;
                            }
                            message.addUser(key, status);
                        }
                    }

                    return message;
                } else {
                    return null;
                }
            } catch(Exception e) {
                return null;
            }
        } else {
            return null;
        }
    } // end of convertString


    /**
     * Adds a new user to the message.
     * 
     * @param user
     *        - new user
     * @param status
     *        - current status
     * @return true in case of success<br>
     *         false in case of the user already added or an error occurs
     */
    public boolean addUser(String user, Status status) {
        if(users.containsKey(user) || status == null) {
            return false;
        } else {
            users.put(user, status);
            return true;
        }
    } // end of addUser


    /**
     * Returns the status of the given user.
     * 
     * @param key
     *        - nickname of the user
     * @return status of the user or null if no value is mapped to the given key
     */
    public Status getStatus(String key) {
        return users.get(key);
    } // end of getStatus


    /**
     * Returns the iterator of the user map.
     * 
     * @return iterator of type string
     */
    public Iterator<String> getUsers() {
        return users.keySet().iterator();
    } // end of getUserList


    @Override
    public String getMessage() {
        JSONObject message = new JSONObject();
        Iterator<String> iterator = users.keySet().iterator();

        message.put(TYPE, messageData.get(TYPE));

        while(iterator.hasNext()) {
            String key = iterator.next();
            message.put(key, users.get(key).toString().toLowerCase());
        }

        return message.toString();
    } // end of toString
      // end of methods
} // end of class
