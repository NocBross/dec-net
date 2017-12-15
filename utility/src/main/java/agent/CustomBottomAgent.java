package main.java.agent;

import javafx.scene.Parent;
import main.java.constants.AgentID;
import main.java.message.Message;
import main.java.message.RDFMessage;

public class CustomBottomAgent extends CustomAgent {

    protected Parent rootSceneNode;
    protected RootController agentSceneController;

    public CustomBottomAgent(CustomAgent parent, AgentID ID) {
        super(parent, ID);

        rootSceneNode = null;
        agentSceneController = null;
    }

    @Override
    public Parent getScene() {
        return rootSceneNode;
    }

    @Override
    public void receiveMessage(String message) {
        agentSceneController.receiveResult(message);
    }

    @Override
    public void scatterMessage(String message) {
        parent.scatterMessage(message);
    }

    @Override
    public void sendMessage(String url, Message message) {
        parent.sendMessage(url, message);
    }

    @Override
    public void storeRDFModel(RDFMessage message) {
        parent.storeRDFModel(message);
    }

    @Override
    public void switchAgent(AgentID destinationAgent) {
        parent.switchAgent(destinationAgent);
    }

}
