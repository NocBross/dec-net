package main.java;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import main.java.agent.CustomAgent;
import main.java.agent.CustomMiddleAgent;
import main.java.constants.AgentID;
import main.java.controller.SocNetSceneController;

public class SocNetAgent extends CustomMiddleAgent {

    private Parent rootSceneNode;
    private SocNetSceneController agentSceneController;

    public SocNetAgent(CustomAgent parent, Stage primaryStage) {
        super(parent, AgentID.SOCNET_AGENT, primaryStage);

        children.add(new FriendshipAgent(this));
        activeAgent = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SocNetScene.fxml"));
            rootSceneNode = loader.load();
            agentSceneController = (SocNetSceneController) loader.getController();
            agentSceneController.loadChildren(children);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Parent getChildScene(int index) {
        return children.get(index).getScene();
    }

    @Override
    public Parent getScene() {
        return rootSceneNode;
    }

}
