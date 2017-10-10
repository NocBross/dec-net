package main.java;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import main.java.agent.CustomAgent;
import main.java.agent.CustomBottomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;

public class FriendshipAgent extends CustomBottomAgent {

    public FriendshipAgent(CustomAgent parent) {
        super(parent, AgentID.FRIENDSHIP_AGENT);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/FriendshipScene.fxml"));
            rootSceneNode = loader.load();
            agentSceneController = (RootController) loader.getController();
            agentSceneController.setAgent(this);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
