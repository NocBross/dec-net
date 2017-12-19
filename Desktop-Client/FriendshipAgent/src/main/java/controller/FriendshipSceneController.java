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

public class FriendshipSceneController extends FriendshipSceneComponents implements RootController {

    private FriendshipAgent agent;
    private Profiles profiles;

    @FXML
    public void initialize() {
        agent = null;
        profiles = Profiles.getInstance();

        friendList.setItems(profiles.getObservableFriends());
        friendPane.setExpanded(true);
        // groupPane.setVisible(false);

        scrollPane.setVvalue(-0.05);
    }

    @Override
    public void receiveResult(String message) {
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

    /**
     * Opens the search dialog with friend parameters.
     * 
     * @param event
     *            - button clicked
     */
    @FXML
    protected void newFriendButtonAction(ActionEvent event) {
        profiles.setSearchType(SearchType.FRIEND);
        agent.showSearchDialog("neuen Freund hinzufügen");
    }

    @FXML
    protected void onDeleteFriend(ActionEvent event) {
        String nickname = friendList.getSelectionModel().getSelectedItem();
        profiles.deleteFriend(nickname);
        agent.storeRDFModel();
        agent.sendUpdate(nickname);
    }

    @FXML
    protected void openFriendListContext(MouseEvent event) {
        if (event.isPopupTrigger()) {
            friendPane.getContextMenu().show(friendPane, event.getX(), event.getY());
        }
    }

    private void handleUserIDMessage(String message) {
        UserIDMessage userIDMessage = UserIDMessage.parse(message);
        if (userIDMessage != null) {
            profiles.setActiveUser(userIDMessage.getUserID());
            friendList.setItems(profiles.getObservableFriends());
        }
    }

    private void handlePostMessage(String message) {
        PostMessage postMessage = PostMessage.parse(message);
        if (postMessage != null) {
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
                }
            }
        }
    }

    private void handleSearchMessage(String message) {
        SearchMessage resultMessage = SearchMessage.parse(message);
        if (resultMessage != null) {
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
            agent.showResultDialog(header, resultMessage.getNicknames());
        }
    }

}
