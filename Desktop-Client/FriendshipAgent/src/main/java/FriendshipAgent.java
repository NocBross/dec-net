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
import main.java.abstraction.ProfileRDF;
import main.java.abstraction.UserProfile;
import main.java.agent.CustomAgent;
import main.java.agent.CustomBottomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;
import main.java.controller.AddToFriendshipGroupController;
import main.java.controller.NewFriendshipGroupDialogController;
import main.java.controller.ResultDialogController;
import main.java.controller.SearchDialogController;

public class FriendshipAgent extends CustomBottomAgent {

    private boolean addFriendshipGroupDialogIsOpen;
    private boolean newFriendshipGroupDialogIsOpen;
    private boolean resultDialogIsOpen;
    private boolean searchDialogIsOpen;
    private Stage addToFriendshipGroupDialog;
    private Stage newFriendshipGroupDialog;
    private Stage resultDialogStage;
    private Stage searchDialogStage;
    private AddToFriendshipGroupController addToFriendshipGroupController;
    private NewFriendshipGroupDialogController newFriendshipGroupDialogController;
    private ResultDialogController resultDialogController;
    private SearchDialogController searchDialogController;


    public FriendshipAgent(CustomAgent parent) {
        super(parent, AgentID.FRIENDSHIP_AGENT);
        addFriendshipGroupDialogIsOpen = false;
        newFriendshipGroupDialogIsOpen = false;
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

            loader = new FXMLLoader(getClass().getResource("/ui/AddToFriendshipGroupDialog.fxml"));
            createAddToFriendshipGroupDialog(loader.load());
            addToFriendshipGroupController = (AddToFriendshipGroupController) loader.getController();
            addToFriendshipGroupController.setAgent(this);

            loader = new FXMLLoader(getClass().getResource("/ui/NewFriendshipGroupDialog.fxml"));
            createNewFriendshipGroupDialog(loader.load());
            newFriendshipGroupDialogController = (NewFriendshipGroupDialogController) loader.getController();
            newFriendshipGroupDialogController.setAgent(this);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }


    public void closeAddToFriendshipGroupDialog() {
        addToFriendshipGroupDialog.close();
        toggleAddFriendshipGroupDialogIsOpen();
    }


    public void closeNewFriendshipGroupDialog() {
        newFriendshipGroupDialog.close();
        toggleNewFriendsgipGroupDialogIsOpen();
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


    public void showAddToFriendshipGroupDialog(String selectedNickname) {
        try {
            if( !addFriendshipGroupDialogIsOpen) {
                addToFriendshipGroupController.setSelectedNickname(selectedNickname);
                addToFriendshipGroupController.updateFriendshipGroupList();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        addToFriendshipGroupDialog.show();
                        toggleAddFriendshipGroupDialogIsOpen();
                    }

                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void showNewFriendshipGroupDialog(String selectedNickname) {
        try {
            if( !newFriendshipGroupDialogIsOpen) {
                newFriendshipGroupDialogController.setSelectedNickname(selectedNickname);
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        newFriendshipGroupDialog.show();
                        toggleNewFriendsgipGroupDialogIsOpen();
                    }

                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void showResultDialog(String headerText, List<String> resultList) {
        try {
            if( !resultDialogIsOpen) {
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
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void showSearchDialog(String headerText) {
        if( !searchDialogIsOpen) {
            searchDialogController.setHeader(headerText);
            searchDialogStage.show();
            toggleSearchDialogIsOpen();
        }
    }


    private void createAddToFriendshipGroupDialog(Parent resultDialog) {
        addToFriendshipGroupDialog = new Stage();
        addToFriendshipGroupDialog.initStyle(StageStyle.UTILITY);
        addToFriendshipGroupDialog.setScene(new Scene(resultDialog, 300, 100));
        addToFriendshipGroupDialog.centerOnScreen();
        addToFriendshipGroupDialog.setAlwaysOnTop(true);
        addToFriendshipGroupDialog.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                addToFriendshipGroupController.close();
            }

        });
    }


    private void createNewFriendshipGroupDialog(Parent resultDialog) {
        newFriendshipGroupDialog = new Stage();
        newFriendshipGroupDialog.initStyle(StageStyle.UTILITY);
        newFriendshipGroupDialog.setScene(new Scene(resultDialog, 300, 100));
        newFriendshipGroupDialog.centerOnScreen();
        newFriendshipGroupDialog.setAlwaysOnTop(true);
        newFriendshipGroupDialog.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                newFriendshipGroupDialogController.close();
            }

        });
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


    private void toggleAddFriendshipGroupDialogIsOpen() {
        addFriendshipGroupDialogIsOpen = !addFriendshipGroupDialogIsOpen;
    }


    private void toggleNewFriendsgipGroupDialogIsOpen() {
        newFriendshipGroupDialogIsOpen = !newFriendshipGroupDialogIsOpen;
    }


    private void toggleResultDialogIsOpen() {
        resultDialogIsOpen = !resultDialogIsOpen;
    }


    private void toggleSearchDialogIsOpen() {
        searchDialogIsOpen = !searchDialogIsOpen;
    }

}
