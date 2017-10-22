package main.java.controller;

import java.io.ByteArrayInputStream;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import main.java.FriendshipAgent;
import main.java.abstraction.FriendshipSceneComponents;
import main.java.abstraction.ProfileRDF;
import main.java.abstraction.SearchType;
import main.java.abstraction.UserProfile;
import main.java.agent.CustomAgent;
import main.java.agent.RootController;
import main.java.message.LoginMessage;
import main.java.message.RDFMessage;
import main.java.message.SearchMessage;

public class FriendshipSceneController extends FriendshipSceneComponents implements RootController {

    private FriendshipAgent agent;
    private ProfileRDF rdfModel;
    private UserProfile profile;


    @FXML
    public void initialize() {
        agent = null;
        rdfModel = ProfileRDF.getInstance();
        profile = UserProfile.getInstance();

        directMessageList.setItems(profile.getDirectMessages());
        directMessagePane.setExpanded(true);
        friendList.setItems(profile.getFriends());
        friendPane.setExpanded(false);
        groupPane.setVisible(false);

        scrollPane.setVvalue( -0.05);
    }


    @Override
    public void receiveResult(String message) {
        handleLoginMessage(message);
        handleRDFMessage(message);
        handleSearchMessage(message);
    }


    @Override
    public void setAgent(CustomAgent newAgent) {
        if(newAgent instanceof FriendshipAgent) {
            agent = (FriendshipAgent) newAgent;
        }
    }


    /**
     * Opens the search dialog with direct message parameters.
     * 
     * @param event
     *        - button clicked
     */
    @FXML
    protected void newDirectMessageButtonAction(ActionEvent event) {
        profile.setSearchType(SearchType.DIRECT_MESSAGE);
        agent.showSearchDialog("neue Direktnachricht hinzufügen");
    }


    /**
     * Opens the search dialog with friend parameters.
     * 
     * @param event
     *        - button clicked
     */
    @FXML
    protected void newFriendButtonAction(ActionEvent event) {
        profile.setSearchType(SearchType.FRIEND);
        agent.showSearchDialog("neuen Freund hinzufügen");
    }


    @FXML
    protected void onAddNewFriendshipGroup(ActionEvent event) {
        agent.showNewFriendshipGroupDialog(friendList.getSelectionModel().getSelectedItem());
    }


    @FXML
    protected void onAddToFriendshipGroup(ActionEvent event) {
        agent.showAddToFriendshipGroupDialog(friendList.getSelectionModel().getSelectedItem());
    }


    @FXML
    protected void onDeleteDirect(ActionEvent event) {
        String nickname = directMessageList.getSelectionModel().getSelectedItem();
        profile.deleteDirectMessage(nickname);
        rdfModel.deleteDirectMessage(profile.getUserID(), nickname);
        agent.storeRDFModel();
    }


    @FXML
    protected void onDeleteFriend(ActionEvent event) {
        String nickname = friendList.getSelectionModel().getSelectedItem();
        profile.deleteFriend(nickname);
        rdfModel.deleteFriend(profile.getUserID(), nickname);
        agent.storeRDFModel();
    }


    @FXML
    protected void onMoveToDirect(ActionEvent event) {
        String nickname = friendList.getSelectionModel().getSelectedItem();
        profile.addDirectMessage(nickname);
        rdfModel.addNewDirectMessage(profile.getUserID(), nickname);
        agent.storeRDFModel();
    }


    @FXML
    protected void onMoveToFriend(ActionEvent event) {
        String nickname = directMessageList.getSelectionModel().getSelectedItem();
        profile.addFriend(nickname);
        rdfModel.addNewFriend(profile.getUserID(), nickname);
        agent.storeRDFModel();
    }


    @FXML
    protected void openDirectMessageListContext(MouseEvent event) {
        if(event.isPopupTrigger()) {
            directMessagePane.getContextMenu().show(directMessagePane, event.getX(), event.getY());
        }
    }


    @FXML
    protected void openFriendListContext(MouseEvent event) {
        if(event.isPopupTrigger()) {
            friendPane.getContextMenu().show(friendPane, event.getX(), event.getY());
        }
    }


    private void handleLoginMessage(String message) {
        LoginMessage loginMessage = LoginMessage.parse(message);
        if(loginMessage != null) {
            profile.setUserID(loginMessage.getNickname());
        }
    }


    private void handleRDFMessage(String message) {
        RDFMessage rdfMessage = RDFMessage.parse(message);
        if(rdfMessage != null) {
            ResultSet result = null;
            ByteArrayInputStream stringReader = new ByteArrayInputStream(rdfMessage.getModel().getBytes());
            rdfModel.addModel(stringReader);

            result = rdfModel.getDirectMessages();
            if(result != null) {
                while(result.hasNext()) {
                    QuerySolution solution = result.next();
                    profile.addDirectMessage(solution.getResource("nickname").getLocalName());
                }
            }

            result = rdfModel.getFriends();
            if(result != null) {
                while(result.hasNext()) {
                    QuerySolution solution = result.next();
                    profile.addFriend(solution.getResource("nickname").getLocalName());
                }
            }

            result = rdfModel.getFriendshipGroupsWithMembers();
            if(result != null) {
                while(result.hasNext()) {
                    QuerySolution solution = result.next();
                    profile.addFriendToFriendshipGroup(solution.getResource("nickname").getLocalName(), solution
                            .getResource("friendshipGroup").getLocalName());
                }
            }
        }
    }


    private void handleSearchMessage(String message) {
        SearchMessage resultMessage = SearchMessage.parse(message);
        if(resultMessage != null) {
            String header = "";
            switch(profile.getSearchType()) {
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
