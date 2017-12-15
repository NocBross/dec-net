package main.java.controller;

import java.util.LinkedList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.FriendshipAgent;
import main.java.abstraction.Profiles;
import main.java.abstraction.ResultDialogComponents;

public class ResultDialogController extends ResultDialogComponents {

    private FriendshipAgent agent;
    private Profiles profiles;

    @FXML
    public void initialize() {
        agent = null;
        profiles = Profiles.getInstance();
    }

    public void close() {
        agent.closeResultDialog();
        nameChoiceBox.getItems().clear();
    }

    public void setAgent(FriendshipAgent newAgent) {
        agent = newAgent;
    }

    public void setHeader(String header) {
        resultHeaderText.setText(header);
    }

    public void setResults(List<String> resultList) {
        List<String> filteredResultList = null;
        resultList.remove(profiles.getActiveUser());

        switch (profiles.getSearchType()) {
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
        close();
    }

    @FXML
    protected void addButtonAction(ActionEvent event) {
        String userID = nameChoiceBox.getValue();

        switch (profiles.getSearchType()) {
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

        for (int i = 0; i < unfilteredList.size(); i++) {
            if (!referenceList.contains(unfilteredList.get(i))) {
                filteredList.add(unfilteredList.get(i));
            }
        }

        return filteredList;
    }

}
