package main.java;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import main.java.agent.CustomAgent;
import main.java.agent.CustomMiddleAgent;
import main.java.constants.AgentID;
import main.java.constants.ClientLogs;
import main.java.controller.SocNetSceneController;

public class SocNetAgent extends CustomMiddleAgent {

    private Parent rootSceneNode;
    private SocNetSceneController agentSceneController;


    public SocNetAgent(CustomAgent parent, Stage primaryStage) throws Exception {
        super(parent, AgentID.SOCNET_AGENT, primaryStage, ClientLogs.SOC_NET_AGENT, "SocNetAgent");

        children.add(0, new FriendshipAgent(this));
        children.add(1, new PinboardAgent(this));
        activeAgent = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SocNetScene.fxml"));
            rootSceneNode = loader.load();
            agentSceneController = (SocNetSceneController) loader.getController();
            agentSceneController.loadChildren(children);
        } catch(IOException ioe) {
            logger.writeLog(logID + " error while loading SocNetAgent", ioe);
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
