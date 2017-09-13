package main.java.agent;

import javafx.scene.Parent;
import javafx.scene.Scene;
import main.java.constants.LTAgentID;

public class CustomBottomAgent extends LTAgent {

	protected Parent rootAgent;
	protected Scene agentScene;
	protected RootController agentSceneController;

	public CustomBottomAgent(LTAgent parent, LTAgentID ID) {
		super(parent, ID);
	}

	@Override
	public Scene getScene() {
		return agentScene;
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
	public void switchAgent(LTAgentID destinationAgent) {
		parent.switchAgent(destinationAgent);
	}

}
