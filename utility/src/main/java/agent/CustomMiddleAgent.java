package main.java.agent;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.stage.Stage;
import main.java.constants.AgentID;
import main.java.message.Message;
import main.java.message.RDFMessage;

public class CustomMiddleAgent extends CustomAgent {

    protected CustomAgent activeAgent;
    protected Stage primaryStage;


    public CustomMiddleAgent(CustomAgent parent, AgentID ID, Stage primaryStage, String logFilePath, String logID)
            throws Exception {
        super(parent, ID, logFilePath, logID);
        this.primaryStage = primaryStage;
    }


    @Override
    public Parent getScene() {
        try {
            return activeAgent.getScene();
        } catch(NullPointerException npe) {
            return null;
        }
    }


    @Override
    public void receiveMessage(String message) {
        logger.writeLog("received message " + message, null);
        for(int i = 0; i < children.size(); i++ ) {
            children.get(i).receiveMessage(message);
        }
    }


    @Override
    public void scatterMessage(String message) {
        logger.writeLog("scattering message " + message, null);
        receiveMessage(message);
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
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                int index = 0;

                for(index = 0; index < children.size(); index++ ) {
                    if(children.get(index).getID() == destinationAgent) {
                        primaryStage.getScene().setRoot(children.get(index).getScene());
                        activeAgent = children.get(index);
                        break;
                    }
                }

                if(index == children.size()) {
                    parent.switchAgent(destinationAgent);
                }
            }

        });
    }

}
