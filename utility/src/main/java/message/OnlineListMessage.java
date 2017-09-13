package main.java.message;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * This message is used by the client to get all friend which are currently
 * online from the server.
 * 
 * @author Kay Oliver Szesny
 *
 */

public class OnlineListMessage extends LTMessage {

	public static final String ID = "online-list";
	private static final String USER_LIST = "user-list";
	private List<String> users;

	public OnlineListMessage() {
		super(ID);
		users = new LinkedList<String>();
	}

	/**
	 * Creates a LoginMessage from a given string.
	 * 
	 * @param messageString
	 *            - string which will be converted
	 * @return LoginMessage of the string or null if the string not representing a
	 *         LoginMessage
	 */
	public static OnlineListMessage parse(String messageString) {
		if (messageString != null) {
			try {
				JSONTokener parser = new JSONTokener(messageString);
				JSONObject jsonMessage = (JSONObject) parser.nextValue();

				if (jsonMessage.getString(TYPE).equals(ID)) {
					OnlineListMessage message = new OnlineListMessage();

					JSONArray userList = jsonMessage.getJSONArray(USER_LIST);
					for (int i = 0; i < userList.length(); i++) {
						message.addUser(userList.getString(i));
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
	 * Adds a new user to the message.
	 * 
	 * @param user
	 *            - new user
	 * @return true in case of success<br>
	 *         false in case of the user already added or an error occurs
	 */
	public boolean addUser(String user) {
		if (users.contains(user)) {
			return false;
		} else {
			users.add(user);
			return true;
		}
	}

	/**
	 * Returns the user at the given index.
	 * 
	 * @param index
	 *            - user position in the list
	 * @return user at the position or null in case of
	 *         ArrayIndexOutOufBoundsException
	 */
	public String getUser(int index) {
		try {
			return users.get(index);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns the map of users.
	 * 
	 * @return iterator of type string
	 */
	public Iterator<String> getUsers() {
		return users.iterator();
	}

	@Override
	public String getMessage() {
		JSONObject message = new JSONObject();
		JSONArray userList = new JSONArray();
		Iterator<String> iterator = users.iterator();

		message.put(TYPE, messageData.get(TYPE));

		while (iterator.hasNext()) {
			String key = iterator.next();
			userList.put(key);
		}
		message.put(USER_LIST, userList);

		return message.toString();
	}
}
