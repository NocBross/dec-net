package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.java.FriendshipAgent;
import main.java.abstraction.NewFriendshipGroupDialogComponents;
import main.java.abstraction.ProfileRDF;
import main.java.abstraction.UserProfile;

public class NewFriendshipGroupDialogController extends NewFriendshipGroupDialogComponents {

    private String selectedNickname;
    private FriendshipAgent agent;
    private ProfileRDF rdfModel;
    private UserProfile profile;


    @FXML
    public void initialize() {
        selectedNickname = null;
        agent = null;
        rdfModel = ProfileRDF.getInstance();
        profile = UserProfile.getInstance();
    }


    public void close() {
        agent.closeNewFriendshipGroupDialog();
        friendshipGroupTextField.setText("");
    }


    public void setAgent(FriendshipAgent newAgent) {
        agent = newAgent;
    }


    public void setSelectedNickname(String nickname) {
        selectedNickname = nickname;
    }


    @FXML
    protected void addButtonAction(ActionEvent event) {
        addNewFriendshipGroup();
    }


    @FXML
    protected void cancelButtonAction(ActionEvent event) {
        close();
    }


    @FXML
    protected void handleKeyEvent(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            addNewFriendshipGroup();
        }
    }


    private void addNewFriendshipGroup() {
        if( !friendshipGroupTextField.getText().equals("") && selectedNickname != null) {
            String friendshipGroup = friendshipGroupTextField.getText();
            profile.addFriendToFriendshipGroup(selectedNickname, friendshipGroup);
            rdfModel.addFriendToFriendshipGroup(selectedNickname, friendshipGroup);
            agent.storeRDFModel();
            selectedNickname = null;
        }
        close();
    }

}
