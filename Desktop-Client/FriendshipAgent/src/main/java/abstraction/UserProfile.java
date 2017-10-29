package main.java.abstraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserProfile {

    private static UserProfile profile = null;

    private SearchType searchType;
    private String userID;
    private Map<String, List<String>> friendshipGroupMembers;
    private ObservableList<String> directMessages;
    private ObservableList<String> friends;

    private UserProfile() {
        searchType = null;
        userID = null;
        friendshipGroupMembers = new HashMap<String, List<String>>();
        directMessages = FXCollections.observableArrayList();
        friends = FXCollections.observableArrayList();
    }

    public static UserProfile getInstance() {
        if (profile == null) {
            profile = new UserProfile();
        }

        return profile;
    }

    public void addDirectMessage(String nickname) {
        if (!directMessages.contains(nickname)) {
            directMessages.add(nickname);
        }
        if (friends.contains(nickname)) {
            friends.remove(nickname);
        }
    }

    public void addFriend(String nickname) {
        if (!friends.contains(nickname)) {
            friends.add(nickname);
        }
        if (directMessages.contains(nickname)) {
            directMessages.remove(nickname);
        }
    }

    public void addFriendToFriendshipGroup(String nickname, String friendshipGroup) {
        List<String> members = friendshipGroupMembers.get(friendshipGroup);
        if (members == null) {
            friendshipGroupMembers.put(friendshipGroup, new ArrayList<String>(20));
            members = friendshipGroupMembers.get(friendshipGroup);
        }
        members.add(nickname);
    }

    public void deleteDirectMessage(String nickname) {
        directMessages.remove(nickname);
    }

    public void deleteFriend(String nickname) {
        friends.remove(nickname);
    }

    public ObservableList<String> getDirectMessages() {
        return directMessages;
    }

    public ObservableList<String> getFriends() {
        return friends;
    }

    public List<String> getFriendshipGroups() {
        return new ArrayList<String>(friendshipGroupMembers.keySet());
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public String getUserID() {
        return userID;
    }

    public void setSearchType(SearchType type) {
        searchType = type;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
