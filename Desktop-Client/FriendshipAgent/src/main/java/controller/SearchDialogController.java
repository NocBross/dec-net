package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.java.FriendshipAgent;
import main.java.abstraction.Profiles;
import main.java.abstraction.SearchDialogComponents;
import main.java.constants.Network;
import main.java.constants.WebServiceConstants;
import main.java.constants.WebServiceContext;
import main.java.service.CustomLogger;

public class SearchDialogController extends SearchDialogComponents {

    private String logID;
    private FriendshipAgent agent;
    private CustomLogger logger;


    @FXML
    public void initialize() {
        logID = "SearchDialogController";
        agent = null;
    }


    public void close() {
        logger.writeLog(logID + " closing dialog", null);
        agent.closeSearchDialog();
        nicknameTextField.setText("");
    }


    public void setAgent(FriendshipAgent newAgent) {
        agent = newAgent;
    }


    public void setLogger(CustomLogger newLogger) {
        logger = newLogger;
    }


    public void setHeader(String header) {
        searchHeaderText.setText(header);
    }


    @FXML
    protected void cancelButtonAction(ActionEvent event) {
        logger.writeLog(logID + " cancel button clicked", null);
        close();
    }


    @FXML
    protected void searchButtonAction(ActionEvent event) {
        logger.writeLog(logID + " search button clicked", null);
        sendSearchRequest();
    }


    @FXML
    protected void handleKeyEvent(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            logger.writeLog(logID + " sending search request by key event", null);
            sendSearchRequest();
        }
    }


    private void sendSearchRequest() {
        logger.writeLog(logID + " sending search request", null);
        if( !nicknameTextField.getText().equals("")) {
            String[] userID = nicknameTextField.getText().split("@");
            if(userID.length != 2) {
                userID = Profiles.getInstance().getActiveUser().split("@");
            }

            String url = Network.NETWORK_PROTOCOL + userID[1] + ":" + Network.SERVER_WEBSERVICE_PORT
                    + WebServiceContext.SEARCH + WebServiceConstants.CONTEXT_SEPARATOR + WebServiceConstants.USER_ID_KEY
                    + WebServiceConstants.KEY_VALUE_SEPARATOR + nicknameTextField.getText();
            agent.sendMessage(url, null);
        }
        close();
    }

}
