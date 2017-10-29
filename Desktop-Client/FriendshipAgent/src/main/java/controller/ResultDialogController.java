package main.java.controller;

import java.util.LinkedList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.FriendshipAgent;
import main.java.abstraction.ProfileRDF;
import main.java.abstraction.ResultDialogComponents;
import main.java.abstraction.UserProfile;

public class ResultDialogController extends ResultDialogComponents {

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
        resultList.remove(profile.getUserID());

        switch (profile.getSearchType()) {
            case DIRECT_MESSAGE:
                filteredResultList = filterList(resultList, profile.getDirectMessages());
                break;
            case FRIEND:
                filteredResultList = filterList(resultList, profile.getFriends());
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
        String nickname = nameChoiceBox.getValue();

        switch (profile.getSearchType()) {
            case DIRECT_MESSAGE:
                profile.addDirectMessage(nickname);
                rdfModel.addNewDirectMessage(profile.getUserID(), nickname);
                break;
            case FRIEND:
                profile.addFriend(nickname);
                rdfModel.addNewFriend(profile.getUserID(), nickname);
                break;
            default:
                break;
        }
        agent.storeRDFModel();

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
