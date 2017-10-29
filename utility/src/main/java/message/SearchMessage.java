package main.java.message;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SearchMessage extends Message {

	public static final String ID = "search";
	private static final String REUQUESTED_NICKNAMES = "nicknames";
	private List<String> nicknameList;

	public SearchMessage() {
		super(ID);
		nicknameList = new LinkedList<String>();
	}

	/**
	 * Creates a SearchMessage from a given string.
	 * 
	 * @param messageString
	 *            - string which will be converted
	 * @return SearchMessage of the string or null if the string not representing a
	 *         SearchMessage
	 */
	public static SearchMessage parse(String messageString) {
		if (messageString != null) {
			try {
				JSONTokener parser = new JSONTokener(messageString);
				JSONObject jsonMessage = (JSONObject) parser.nextValue();

				if (jsonMessage.getString(TYPE).equals(ID)) {
					SearchMessage message = new SearchMessage();

					JSONArray nicknameList = jsonMessage.getJSONArray(REUQUESTED_NICKNAMES);
					for (int i = 0; i < nicknameList.length(); i++) {
						message.addNickname(nicknameList.getString(i));
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

	public boolean addNickname(String nickname) {
		if (nicknameList.contains(nickname)) {
			return false;
		} else {
			nicknameList.add(nickname);
			return true;
		}
	}

	public List<String> getNicknames() {
		return nicknameList;
	}

	@Override
	public String getMessage() {
		JSONObject message = new JSONObject();
		JSONArray request = new JSONArray();

		message.put(TYPE, ID);
		for (int i = 0; i < nicknameList.size(); i++) {
			request.put(nicknameList.get(i));
		}
		message.put(REUQUESTED_NICKNAMES, request);

		return message.toString();
	}
}
