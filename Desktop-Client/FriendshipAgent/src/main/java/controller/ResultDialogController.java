package main.java.controller;

import java.util.LinkedList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.FriendshipAgent;
import main.java.abstraction.Profiles;
import main.java.abstraction.ResultDialogComponents;
import main.java.service.CustomLogger;

public class ResultDialogController extends ResultDialogComponents {

    private String logID;
    private FriendshipAgent agent;
    private Profiles profiles;
    private CustomLogger logger;


    @FXML
    public void initialize() {
        logID = "ResultDialogController";
        agent = null;
        profiles = Profiles.getInstance();
    }


    public void close() {
        logger.writeLog(logID + " closing dialog", null);
        agent.closeResultDialog();
        nameChoiceBox.getItems().clear();
    }


    public void setAgent(FriendshipAgent newAgent) {
        agent = newAgent;
    }


    public void setLogger(CustomLogger newLogger) {
        logger = newLogger;
    }


    public void setHeader(String header) {
        resultHeaderText.setText(header);
    }


    public void setResults(List<String> resultList) {
        List<String> filteredResultList = null;
        resultList.remove(profiles.getActiveUser());

        switch(profiles.getSearchType()) {
            case FRIEND:
                filteredResultList = filterList(resultList, profiles.getFriends());
                break;
            default:
                break;
        }

        nameChoiceBox.getItems().addAll(filteredResultList);
        nameChoiceBox.getSelectionModel().select(0);
    }


    @FXML
    protected void cancelButtonAction(ActionEvent event) {
        logger.writeLog(logID + " cancel button clicked", null);
        close();
    }


    @FXML
    protected void addButtonAction(ActionEvent event) {
        logger.writeLog(logID + " add button clicked", null);
        String userID = nameChoiceBox.getValue();

        switch(profiles.getSearchType()) {
            case FRIEND:
                profiles.addFriend(userID);
                break;
            default:
                break;
        }
        agent.storeRDFModel();
        agent.sendUpdate(null);

        close();
    }


    private List<String> filterList(List<String> unfilteredList, List<String> referenceList) {
        List<String> filteredList = new LinkedList<String>();

        for(int i = 0; i < unfilteredList.size(); i++ ) {
            if( !referenceList.contains(unfilteredList.get(i))) {
                filteredList.add(unfilteredList.get(i));
            }
        }

        return filteredList;
    }

}
