package main.java.agent;

import javafx.scene.Parent;
import main.java.constants.AgentID;
import main.java.message.Message;
import main.java.message.RDFMessage;

public class CustomBottomAgent extends CustomAgent {

    protected Parent rootSceneNode;
    protected RootController agentSceneController;


    public CustomBottomAgent(CustomAgent parent, AgentID ID, String logFilePath, String logID) throws Exception {
        super(parent, ID, logFilePath, logID);

        rootSceneNode = null;
        agentSceneController = null;
    }


    @Override
    public Parent getScene() {
        return rootSceneNode;
    }


    @Override
    public void receiveMessage(String message) {
        logger.writeLog("received message " + message, null);
        agentSceneController.receiveResult(message);
    }


    @Override
    public void scatterMessage(String message) {
        logger.writeLog("scattering message " + message, null);
        parent.scatterMessage(message);
    }


    @Override
    public void sendMessage(String url, Message message) {
        if(message == null) {
            logger.writeLog("sending GET-Request to " + url, null);
        } else {
            logger.writeLog("sending POST-Request fpr message " + message.getType() + " to " + url, null);
        }
        parent.sendMessage(url, message);
    }


    @Override
    public void storeRDFModel(RDFMessage message) {
        logger.writeLog("storing " + message.getType(), null);
        parent.storeRDFModel(message);
    }


    @Override
    public void switchAgent(AgentID destinationAgent) {
        logger.writeLog("switching agent to " + destinationAgent.toString(), null);
        parent.switchAgent(destinationAgent);
    }

}
