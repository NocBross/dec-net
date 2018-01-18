package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import main.java.FriendshipAgent;
import main.java.abstraction.FriendshipSceneComponents;
import main.java.abstraction.Profiles;
import main.java.abstraction.SearchType;
import main.java.agent.CustomAgent;
import main.java.agent.RootController;
import main.java.constants.Network;
import main.java.constants.WebServiceConstants;
import main.java.message.PostMessage;
import main.java.message.RDFMessage;
import main.java.message.SearchMessage;
import main.java.message.UserIDMessage;
import main.java.service.CustomLogger;

public class FriendshipSceneController extends FriendshipSceneComponents implements RootController {

    private String logID;
    private FriendshipAgent agent;
    private Profiles profiles;
    private CustomLogger logger;

    @FXML
    public void initialize() {
        logID = "FriendshipSceneController";
        agent = null;
        profiles = Profiles.getInstance();

        friendList.setItems(profiles.getObservableFriends());
        friendPane.setExpanded(true);
        // groupPane.setVisible(false);

        scrollPane.setVvalue(-0.05);
    }

    @Override
    public void receiveResult(String message) {
        logger.writeLog(logID + " received message " + message, null);
        handleUserIDMessage(message);
        handlePostMessage(message);
        handleRDFMessage(message);
        handleSearchMessage(message);
    }

    @Override
    public void setAgent(CustomAgent newAgent) {
        if (newAgent instanceof FriendshipAgent) {
            agent = (FriendshipAgent) newAgent;
        }
    }

    @Override
    public void setLogger(CustomLogger newlogger) {
        logger = newlogger;
    }

    /**
     * Opens the search dialog with friend parameters.
     * 
     * @param event
     *            - button clicked
     */
    @FXML
    protected void newFriendButtonAction(ActionEvent event) {
        logger.writeLog(logID + " new friend button clicked", null);
        profiles.setSearchType(SearchType.FRIEND);
        agent.showSearchDialog("neuen Freund hinzufügen");
    }

    @FXML
    protected void onDeleteFriend(ActionEvent event) {
        logger.writeLog(logID + " on delete friend clicked", null);
        String nickname = friendList.getSelectionModel().getSelectedItem();
        profiles.deleteFriend(nickname);
        agent.storeRDFModel();
        agent.sendUpdate(nickname);
    }

    @FXML
    protected void onShowPosts(ActionEvent event) {
        logger.writeLog(logID + " on show posts clicked", null);
        String nickname = friendList.getSelectionModel().getSelectedItem();
        PostMessage postMessage = new PostMessage();
        postMessage.setPostList(profiles.getPosts(nickname));
        postMessage.setUpdateProfile(false);
        agent.scatterMessage(postMessage.getMessage());
    }

    @FXML
    protected void openFriendListContext(MouseEvent event) {
        if (event.isPopupTrigger()) {
            logger.writeLog(logID + " open friend list context menu", null);
            friendPane.getContextMenu().show(friendPane, event.getX(), event.getY());
        }
    }

    private void handleUserIDMessage(String message) {
        UserIDMessage userIDMessage = UserIDMessage.parse(message);
        if (userIDMessage != null) {
            logger.writeLog(logID + " processing " + userIDMessage.getType(), null);
            profiles.setActiveUser(userIDMessage.getUserID());
            friendList.setItems(profiles.getObservableFriends());
        }
    }

    private void handlePostMessage(String message) {
        PostMessage postMessage = PostMessage.parse(message);
        if (postMessage != null) {
            logger.writeLog(logID + " processing " + postMessage.getType(), null);
            if (postMessage.getUpdateProfile()) {
                profiles.addPost(postMessage.getPosts().get(0));
                agent.storeRDFModel();
                agent.sendUpdate(null);
            }
        }
    }

    private void handleRDFMessage(String message) {
        RDFMessage rdfMessage = RDFMessage.parse(message);
        if (rdfMessage != null) {
            logger.writeLog(logID + " processing " + rdfMessage.getType(), null);
            if (rdfMessage.getResourceID().indexOf(WebServiceConstants.PROFILE_RESOURCE) != -1) {
                int addressIndex = 1;
                String[] splittetURL = rdfMessage.getResourceID().replace(Network.NETWORK_PROTOCOL, "").split("/");

                if (splittetURL[0] == null || splittetURL[0].equals("")) {
                    addressIndex++;
                }

                String userID = splittetURL[addressIndex] + "@" + splittetURL[addressIndex - 1];
                profiles.setModel(userID, rdfMessage.getModel());

                if (userID.equals(profiles.getActiveUser())) {
                    PostMessage postMessage = new PostMessage();
                    postMessage.setPostList(profiles.getPosts());
                    postMessage.setUpdateProfile(false);
                    agent.scatterMessage(postMessage.getMessage());

                    for (String ID : profiles.getFriends()) {
                        String[] splittedID = ID.split("@");
                        String url = Network.NETWORK_PROTOCOL + splittedID[1] + ":" + Network.SERVER_WEBSERVICE_PORT
                                + "/" + splittedID[0] + WebServiceConstants.PROFILE_RESOURCE;
                        agent.sendMessage(url, null);
                    }
                }
            }
        }
    }

    private void handleSearchMessage(String message) {
        SearchMessage searchMessage = SearchMessage.parse(message);
        if (searchMessage != null) {
            logger.writeLog(logID + " processing " + searchMessage.getType(), null);
            String header = "";
            switch (profiles.getSearchType()) {
                case DIRECT_MESSAGE:
                case FRIEND:
                    header = "Gefundene Personen:";
                    break;
                case GROUP:
                    header = "Gefundene Gruppen:";
                default:
                    break;
            }
            agent.showResultDialog(header, searchMessage.getNicknames());
        }
    }

}
