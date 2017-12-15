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

    public CustomMiddleAgent(CustomAgent parent, AgentID ID, Stage primaryStage) {
        super(parent, ID);
        this.primaryStage = primaryStage;
    }

    @Override
    public Parent getScene() {
        try {
            return activeAgent.getScene();
        } catch (NullPointerException npe) {
            return null;
        }
    }

    @Override
    public void receiveMessage(String message) {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).receiveMessage(message);
        }
    }

    @Override
    public void scatterMessage(String message) {
        receiveMessage(message);
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
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                int index = 0;

                for (index = 0; index < children.size(); index++) {
                    if (children.get(index).getID() == destinationAgent) {
                        primaryStage.getScene().setRoot(children.get(index).getScene());
                        activeAgent = children.get(index);
                        break;
                    }
                }

                if (index == children.size()) {
                    parent.switchAgent(destinationAgent);
                }
            }

        });
    }

}
