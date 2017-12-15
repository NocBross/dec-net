package main.java.message;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class PostMessage extends Message {

    public static final String ID = "post";
    private static final String POST_LIST = "post_list";
    private static final String UPDATE_PROFILE = "new_post";

    public PostMessage() {
        super(ID);
    }

    public static PostMessage parse(String messageString) {
        if (messageString != null) {
            try {
                JSONTokener parser = new JSONTokener(messageString);
                JSONObject jsonMessage = (JSONObject) parser.nextValue();

                if (jsonMessage.getString(TYPE).equals(ID)) {
                    PostMessage message = new PostMessage();

                    message.setPostList(new JSONArray(jsonMessage.getString(POST_LIST)));
                    message.setUpdateProfile(jsonMessage.getBoolean(UPDATE_PROFILE));

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

    public List<String> getPosts() {
        List<String> posts = new LinkedList<String>();
        if (messageData.get(POST_LIST) != null) {
            try {
                JSONArray postArray = new JSONArray(messageData.get(POST_LIST)).getJSONArray(0);

                for (int i = 0; i < postArray.length(); i++) {
                    posts.add(postArray.getString(i));
                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }

        return posts;
    }

    public boolean getUpdateProfile() {
        if (messageData.get(UPDATE_PROFILE) != null) {
            if (messageData.get(UPDATE_PROFILE).equals("true")) {
                return true;
            }
        }

        return false;
    }

    public void setPostList(JSONArray postArray) {
        messageData.put(POST_LIST, postArray.toString());
    }

    public void setPostList(List<String> posts) {
        JSONArray postArray = new JSONArray();
        postArray.put(posts);
        messageData.put(POST_LIST, postArray.toString());
    }

    public void setUpdateProfile(boolean updateProfile) {
        if (updateProfile) {
            messageData.put(UPDATE_PROFILE, "true");
        } else {
            messageData.put(UPDATE_PROFILE, "false");
        }
    }

}
