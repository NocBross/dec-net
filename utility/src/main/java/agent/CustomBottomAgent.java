package main.java.agent;

import org.apache.jena.rdf.model.Model;

import javafx.scene.Parent;
import main.java.constants.AgentID;

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
    public void sendMessage(String destination, String message) {
        parent.sendMessage(destination, message);
    }


    @Override
    public void storeRDFModel(String resourcePath, Model rdfModel) {
        parent.storeRDFModel(resourcePath, rdfModel);
    }


    @Override
    public void switchAgent(AgentID destinationAgent) {
        parent.switchAgent(destinationAgent);
    }

}
