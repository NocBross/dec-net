package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.FriendshipAgent;
import main.java.abstraction.AddToFriendshipGroupComponents;
import main.java.abstraction.ProfileRDF;
import main.java.abstraction.UserProfile;

public class AddToFriendshipGroupController extends AddToFriendshipGroupComponents {

    private String selectedNickname;
    private FriendshipAgent agent;
    private ProfileRDF rdfModel;
    private UserProfile profile;

    @FXML
    public void initialize() {
        agent = null;
        rdfModel = ProfileRDF.getInstance();
        profile = UserProfile.getInstance();
    }

    public void close() {
        agent.closeAddToFriendshipGroupDialog();
        friendshipGroupChoiceBox.getItems().clear();
    }

    public void setAgent(FriendshipAgent newAgent) {
        agent = newAgent;
    }

    public void setSelectedNickname(String nickname) {
        selectedNickname = nickname;
    }

    public void updateFriendshipGroupList() {
        friendshipGroupChoiceBox.getItems().addAll(profile.getFriendshipGroups());
        friendshipGroupChoiceBox.getSelectionModel().select(0);
    }

    @FXML
    protected void cancelButtonAction(ActionEvent event) {
        close();
    }

    @FXML
    protected void addButtonAction(ActionEvent event) {
        if (selectedNickname != null) {
            String friendshipGroup = friendshipGroupChoiceBox.getSelectionModel().getSelectedItem();
            profile.addFriendToFriendshipGroup(selectedNickname, friendshipGroup);
            rdfModel.addFriendToFriendshipGroup(selectedNickname, friendshipGroup);
            agent.storeRDFModel();
            selectedNickname = null;
        }
        close();
    }

}
