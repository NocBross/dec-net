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
import main.java.abstraction.UserProfile;
import main.java.agent.CustomAgent;
import main.java.agent.CustomBottomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;
import main.java.controller.ResultDialogController;
import main.java.controller.SearchDialogController;
import main.java.rdf.ProfileRDF;

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

    public void storeRDFModel() {
        String resourcePath = "/" + UserProfile.getInstance().getUserID() + "/profile";
        parent.storeRDFModel(resourcePath, ProfileRDF.getInstance().getModel());
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
