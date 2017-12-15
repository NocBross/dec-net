package main.java;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import main.java.agent.CustomAgent;
import main.java.agent.CustomBottomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;
import main.java.controller.NewPostDialogController;

public class PinboardAgent extends CustomBottomAgent {

    private boolean newPostDialogIsOpen;
    private Stage newPostDialogStage;
    private NewPostDialogController newPostDialogController;

    public PinboardAgent(CustomAgent parent) {
        super(parent, AgentID.PINBOARD_AGENT);
        newPostDialogIsOpen = false;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/PinboardScene.fxml"));
            rootSceneNode = loader.load();
            agentSceneController = (RootController) loader.getController();
            agentSceneController.setAgent(this);

            loader = new FXMLLoader(getClass().getResource("/ui/NewPostDialog.fxml"));
            createNewPostDialog(loader.load());
            newPostDialogController = (NewPostDialogController) loader.getController();
            newPostDialogController.setAgent(this);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void closeNewPostDialog() {
        newPostDialogStage.close();
        toggleNewPostDialogIsOpen();
    }

    public void showNewPostDialog() {
        if (!newPostDialogIsOpen) {
            newPostDialogStage.show();
            toggleNewPostDialogIsOpen();
        }
    }

    private void createNewPostDialog(Parent newPostDialog) {
        newPostDialogStage = new Stage();
        newPostDialogStage.initStyle(StageStyle.UTILITY);
        newPostDialogStage.setScene(new Scene(newPostDialog, 500, 300));
        newPostDialogStage.centerOnScreen();
        newPostDialogStage.setAlwaysOnTop(true);
        newPostDialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                newPostDialogController.close();
            }

        });
    }

    private void toggleNewPostDialogIsOpen() {
        newPostDialogIsOpen = !newPostDialogIsOpen;
    }

}
