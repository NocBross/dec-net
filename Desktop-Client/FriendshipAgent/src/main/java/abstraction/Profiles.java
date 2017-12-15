package main.java.abstraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.rdf.ProfileRDF;

/**
 * The Profiles contains all profile rdf models linked to a specific user.<br>
 * Furthermore it contains all necessary values to handle user specific
 * manipulations.
 * 
 * @author developer
 *
 */

public class Profiles {

    private static Profiles profiles = null;

    private String activeUserID;
    private SearchType searchType;
    private Map<String, ProfileRDF> models;
    private Map<String, ObservableList<String>> friendLists;

    private Profiles() {
        activeUserID = null;
        models = new HashMap<String, ProfileRDF>();
        friendLists = new HashMap<String, ObservableList<String>>();
    }

    /**
     * Returns the instance of the profiles and creates a new one if no one exists.
     * 
     * @return instance of profiles
     */
    public static Profiles getInstance() {
        if (profiles == null) {
            profiles = new Profiles();
        }

        return profiles;
    }

    /**
     * Adds a new friend to the profile of the current active user.
     * 
     * @param newFriend
     *            - userID of the new friend
     */
    public void addFriend(String newFriend) {
        if (activeUserID != null && !activeUserID.equals("") && newFriend != null && !newFriend.equals("")) {
            addProfile(activeUserID);
            models.get(activeUserID).addFriend(activeUserID, newFriend);
            friendLists.get(activeUserID).add(newFriend);
        }
    }

    /**
     * Adds a new friend to the profile of the given user.
     * 
     * @param userID
     *            - ID of the user
     * @param newFriend
     *            - userID of the new friend
     */
    public void addFriend(String userID, String newFriend) {
        if (userID != null && !userID.equals("") && newFriend != null && !newFriend.equals("")) {
            addProfile(userID);
            models.get(userID).addFriend(userID, newFriend);
            friendLists.get(userID).add(newFriend);
        }
    }

    /**
     * Adds a new post as link to the profile of the current active user.
     * 
     * @param newPostURL
     *            - url of the new post
     */
    public void addPost(String newPostURL) {
        if (activeUserID != null && !activeUserID.equals("") && newPostURL != null && !newPostURL.equals("")) {
            addProfile(activeUserID);
            models.get(activeUserID).addPost(activeUserID, newPostURL);
        }
    }

    /**
     * Adds a new post as link to the profile of the given user.
     * 
     * @param userID
     *            - ID of the user
     * @param newPostURL
     *            - url of the new post
     */
    public void addPost(String userID, String newPostURL) {
        if (userID != null && !userID.equals("") && newPostURL != null && !newPostURL.equals("")) {
            addProfile(userID);
            models.get(userID).addPost(userID, newPostURL);
        }
    }

    /**
     * Creates a new Profile for the current active user
     * 
     * @return true if the profile was successfully created, false otherwise
     */
    public boolean addProfile() {
        if (activeUserID != null && !activeUserID.equals("")) {
            return ((models.putIfAbsent(activeUserID, new ProfileRDF()) == null)
                    && (friendLists.putIfAbsent(activeUserID, FXCollections.observableArrayList()) == null));
        } else {
            return false;
        }
    }

    /**
     * Creates a new Profile for the given user
     * 
     * @param userID
     *            - user which has to get a new profile
     * @return true if the profile was successfully created, false otherwise
     */
    public boolean addProfile(String userID) {
        if (userID != null && !userID.equals("")) {
            return ((models.putIfAbsent(userID, new ProfileRDF()) == null)
                    && (friendLists.putIfAbsent(userID, FXCollections.observableArrayList()) == null));
        } else {
            return false;
        }
    }

    /**
     * Deletes all entries and resets all profiles to initial state.
     */
    public void clearProfiles() {
        profiles = new Profiles();
    }

    /**
     * Deletes the given friend from the friend list of the current active user.
     * 
     * @param userIDOfFriend
     *            - ID of the friend which has to delete
     */
    public void deleteFriend(String userIDOfFriend) {
        if (activeUserID != null && !activeUserID.equals("") && userIDOfFriend != null && !userIDOfFriend.equals("")) {
            addProfile(activeUserID);
            models.get(activeUserID).deleteFriend(activeUserID, userIDOfFriend);
            friendLists.get(activeUserID).remove(userIDOfFriend);
        }
    }

    /**
     * Deletes the given friend from the friend list of the given user.
     * 
     * @param userID
     *            - ID of the user
     * @param userIDOfFriend
     *            - ID of the friend which has to delete
     */
    public void deleteFriend(String userID, String userIDOfFriend) {
        if (userID != null && !userID.equals("") && userIDOfFriend != null && !userIDOfFriend.equals("")) {
            addProfile(userID);
            models.get(userID).deleteFriend(userID, userIDOfFriend);
            friendLists.get(userID).remove(userIDOfFriend);
        }
    }

    /**
     * Deletes the given post from the profile of the current active user.
     * 
     * @param postToDelete
     *            - url of the which has to delete
     */
    public void deletePost(String postToDelete) {
        if (activeUserID != null && !activeUserID.equals("") && postToDelete != null && !postToDelete.equals("")) {
            addProfile(activeUserID);
            models.get(activeUserID).deletePost(activeUserID, postToDelete);
        }
    }

    /**
     * Deletes the given post from the profile of the given user.
     * 
     * @param userID
     *            - ID of the user
     * @param postToDelete
     *            - url of the which has to delete
     */
    public void deletePost(String userID, String postToDelete) {
        if (userID != null && !userID.equals("") && postToDelete != null && !postToDelete.equals("")) {
            addProfile(userID);
            models.get(userID).deletePost(userID, postToDelete);
        }
    }

    /**
     * Returns the current active user.
     * 
     * @return current active user or null if no active user is set
     */
    public String getActiveUser() {
        return activeUserID;
    }

    /**
     * Returns all friends as a list of the current active user.
     * 
     * @return friends as list of the active user or null if no profile for the
     *         active user exists
     */
    public List<String> getFriends() {
        if (activeUserID != null && !activeUserID.equals("")) {
            addProfile(activeUserID);
            return models.get(activeUserID).getFriends();
        }

        return null;
    }

    /**
     * Returns all friends as a list of the given user.
     * 
     * @param userID
     *            - ID of the user
     * @return friends as list of the given user or null if no profile for the given
     *         user exists
     */
    public List<String> getFriends(String userID) {
        if (userID != null && !userID.equals("")) {
            addProfile(userID);
            return models.get(userID).getFriends();
        }

        return null;
    }

    /**
     * Returns the rdf model in xml format of the current active user.
     * 
     * @return rdf model in xml format of the active user or null if no profile
     *         exists
     */
    public String getModel() {
        if (activeUserID != null && !activeUserID.equals("")) {
            addProfile(activeUserID);
            return models.get(activeUserID).getModel();
        }

        return null;
    }

    /**
     * Returns the rdf model in xml format of the given user.
     * 
     * @param userID
     *            - ID of the user
     * @return rdf model in xml format of the given user or null if no profile
     *         exists
     */
    public String getModel(String userID) {
        if (userID != null && !userID.equals("")) {
            addProfile(userID);
            return models.get(userID).getModel();
        }

        return null;
    }

    /**
     * Returns the name which is stored in the profile of the current active user.
     * 
     * @return name of the active user
     */
    public String getName() {
        if (activeUserID != null && !activeUserID.equals("")) {
            addProfile(activeUserID);
            return models.get(activeUserID).getName(activeUserID);
        }

        return null;
    }

    /**
     * Returns the name which is stored in the profile of the given user.
     * 
     * @param userID
     *            - ID of the user
     * @return name of the given user
     */
    public String getName(String userID) {
        if (userID != null && !userID.equals("")) {
            addProfile(userID);
            return models.get(userID).getName(userID);
        }

        return null;
    }

    /**
     * Returns all friends as an observable list of the current active user.
     * 
     * @return friends as observable list of the active user
     */
    public ObservableList<String> getObservableFriends() {
        return friendLists.get(activeUserID);
    }

    /**
     * Returns all friends as an observable list of the given user.
     * 
     * @param userID
     *            - ID of the user
     * @return friends as observable list of the given user
     */
    public ObservableList<String> getObservableFriends(String userID) {
        return friendLists.get(userID);
    }

    /**
     * Returns all posts of the current active user as list.
     * 
     * @return list which contains all posts of the active user
     */
    public List<String> getPosts() {
        if (activeUserID != null && !activeUserID.equals("")) {
            addProfile(activeUserID);
            return models.get(activeUserID).getPosts();
        }

        return null;
    }

    /**
     * Returns all posts of the given user as list.
     * 
     * @return list which contains all posts of the given user
     */
    public List<String> getPosts(String userID) {
        if (userID != null && !userID.equals("")) {
            addProfile(userID);
            return models.get(userID).getPosts();
        }

        return null;
    }

    /**
     * Returns the search type for the current search request.
     * 
     * @return search type for current search request
     */
    public SearchType getSearchType() {
        return searchType;
    }

    /**
     * Sets the current active user to the given one.
     * 
     * @param userID
     *            - new active user
     */
    public void setActiveUser(String userID) {
        if (userID != null && !userID.equals("")) {
            activeUserID = userID;
            addProfile();
        }
    }

    /**
     * Sets the rdf model of the current active user to the given one.
     * 
     * @param serializedModel
     *            - model as xml string
     */
    public void setModel(String serializedModel) {
        if (activeUserID != null && !activeUserID.equals("") && serializedModel != null
                && !serializedModel.equals("")) {
            addProfile(activeUserID);
            models.get(activeUserID).setModel(serializedModel);
            friendLists.get(activeUserID).addAll(models.get(activeUserID).getFriends());
        }
    }

    /**
     * Sets the rdf model of the current active user to the given one.
     * 
     * @param userID
     *            - ID of the user
     * @param serializedModel
     *            - model as xml string
     */
    public void setModel(String userID, String serializedModel) {
        if (userID != null && !userID.equals("") && serializedModel != null && !serializedModel.equals("")) {
            addProfile(userID);
            models.get(userID).setModel(serializedModel);
            friendLists.get(userID).removeAll(models.get(userID).getFriends());
            friendLists.get(userID).addAll(models.get(userID).getFriends());
        }
    }

    /**
     * Sets the name in the profile of the current active user.
     * 
     * @param name
     *            - name of the user
     */
    public void setName(String name) {
        if (activeUserID != null && !activeUserID.equals("") && name != null && !name.equals("")) {
            addProfile(activeUserID);
            models.get(activeUserID).setName(activeUserID, name);
        }
    }

    /**
     * Sets the name in the profile of the given user.
     * 
     * @param userID
     *            - ID of the user
     * @param name
     *            - name of the user
     */
    public void setName(String userID, String name) {
        if (userID != null && !userID.equals("") && name != null && !name.equals("")) {
            addProfile(userID);
            models.get(userID).setName(userID, name);
        }
    }

    /**
     * Sets the search type to update the dialogs.
     * 
     * @param type
     *            - new search type
     */
    public void setSearchType(SearchType type) {
        searchType = type;
    }

}
