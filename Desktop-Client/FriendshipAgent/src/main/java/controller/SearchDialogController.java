package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.java.FriendshipAgent;
import main.java.abstraction.SearchDialogComponents;
import main.java.abstraction.UserProfile;
import main.java.constants.WebServiceConstants;
import main.java.constants.WebServiceContext;

public class SearchDialogController extends SearchDialogComponents {

    private FriendshipAgent agent;

    public void close() {
        agent.closeSearchDialog();
        nicknameTextField.setText("");
    }

    public void setAgent(FriendshipAgent newAgent) {
        agent = newAgent;
    }

    public void setHeader(String header) {
        searchHeaderText.setText(header);
    }

    @FXML
    protected void cancelButtonAction(ActionEvent event) {
        close();
    }

    @FXML
    protected void searchButtonAction(ActionEvent event) {
        sendSearchRequest();
    }

    @FXML
    protected void handleKeyEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendSearchRequest();
        }
    }

    private void sendSearchRequest() {
        if (!nicknameTextField.getText().equals("")) {
            String[] userID = nicknameTextField.getText().split("@");
            if (userID.length != 2) {
                userID = UserProfile.getInstance().getUserID().split("@");
            }

            String resource = WebServiceContext.SEARCH + WebServiceConstants.CONTEXT_SEPARATOR
                    + WebServiceConstants.USER_ID_KEY + WebServiceConstants.KEY_VALUE_SEPARATOR
                    + nicknameTextField.getText();
            agent.sendMessage(userID[1], resource, null);
        }
        close();
    }

}
