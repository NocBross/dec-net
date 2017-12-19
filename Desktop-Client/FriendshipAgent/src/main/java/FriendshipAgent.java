package main.java;

import java.io.IOException;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import main.java.abstraction.Profiles;
import main.java.agent.CustomAgent;
import main.java.agent.CustomBottomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;
import main.java.constants.Network;
import main.java.constants.WebServiceConstants;
import main.java.constants.WebServiceContext;
import main.java.controller.ResultDialogController;
import main.java.controller.SearchDialogController;
import main.java.message.RDFMessage;
import main.java.message.UpdateMessage;

public class FriendshipAgent extends CustomBottomAgent {

    private boolean resultDialogIsOpen;
    private boolean searchDialogIsOpen;
    private Stage resultDialogStage;
    private Stage searchDialogStage;
    private ResultDialogController resultDialogController;
    private SearchDialogController searchDialogController;

    public FriendshipAgent(CustomAgent parent) {
        super(parent, AgentID.FRIENDSHIP_AGENT);
        resultDialogIsOpen = false;
        searchDialogIsOpen = false;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/FriendshipScene.fxml"));
            rootSceneNode = loader.load();
            agentSceneController = (RootController) loader.getController();
            agentSceneController.setAgent(this);

            loader = new FXMLLoader(getClass().getResource("/ui/SearchDialog.fxml"));
            createSearchDialog(loader.load());
            searchDialogController = (SearchDialogController) loader.getController();
            searchDialogController.setAgent(this);

            loader = new FXMLLoader(getClass().getResource("/ui/ResultDialog.fxml"));
            createResultDialog(loader.load());
            resultDialogController = (ResultDialogController) loader.getController();
            resultDialogController.setAgent(this);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void closeResultDialog() {
        resultDialogStage.close();
        toggleResultDialogIsOpen();
    }

    public void closeSearchDialog() {
        searchDialogStage.close();
        toggleSearchDialogIsOpen();
    }

    public void sendUpdate(String deletedFriend) {
        String[] user = Profiles.getInstance().getActiveUser().split("@");
        String resource = Network.NETWORK_PROTOCOL + user[1] + "/" + user[0] + WebServiceConstants.PROFILE_RESOURCE;
        UpdateMessage updateMessage = new UpdateMessage();
        List<String> friends = Profiles.getInstance().getFriends();

        if (deletedFriend != null) {
            friends.add(deletedFriend);
        }

        updateMessage.setResource(resource);
        for (int i = 0; i < friends.size(); i++) {
            String[] friend = friends.get(i).split("@");
            String url = Network.NETWORK_PROTOCOL + friend[1] + WebServiceContext.UPDATE;
            updateMessage.setUserID(friends.get(i));
            sendMessage(url, updateMessage);
        }
    }

    public void storeRDFModel() {
        String[] user = Profiles.getInstance().getActiveUser().split("@");
        String url = Network.NETWORK_PROTOCOL + user[1] + ":" + Network.SERVER_WEBSERVICE_PORT + "/" + user[0]
                + WebServiceConstants.PROFILE_RESOURCE;
        RDFMessage message = new RDFMessage(user[1] + "/" + user[0] + WebServiceConstants.PROFILE_RESOURCE,
                Profiles.getInstance().getModel());

        parent.sendMessage(url, message);
        parent.storeRDFModel(message);
    }

    public void showResultDialog(String headerText, List<String> resultList) {
        try {
            if (!resultDialogIsOpen) {
                resultDialogController.setHeader(headerText);
                resultDialogController.setResults(resultList);
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        resultDialogStage.show();
                        toggleResultDialogIsOpen();
                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSearchDialog(String headerText) {
        if (!searchDialogIsOpen) {
            searchDialogController.setHeader(headerText);
            searchDialogStage.show();
            toggleSearchDialogIsOpen();
        }
    }

    private void createResultDialog(Parent resultDialog) {
        resultDialogStage = new Stage();
        resultDialogStage.initStyle(StageStyle.UTILITY);
        resultDialogStage.setScene(new Scene(resultDialog, 300, 100));
        resultDialogStage.centerOnScreen();
        resultDialogStage.setAlwaysOnTop(true);
        resultDialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                resultDialogController.close();
            }

        });
    }

    private void createSearchDialog(Parent searchDialog) {
        searchDialogStage = new Stage();
        searchDialogStage.initStyle(StageStyle.UTILITY);
        searchDialogStage.setScene(new Scene(searchDialog, 300, 100));
        searchDialogStage.centerOnScreen();
        searchDialogStage.setAlwaysOnTop(true);
        searchDialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                searchDialogController.close();
            }

        });
    }

    private void toggleResultDialogIsOpen() {
        resultDialogIsOpen = !resultDialogIsOpen;
    }

    private void toggleSearchDialogIsOpen() {
        searchDialogIsOpen = !searchDialogIsOpen;
    }

}
