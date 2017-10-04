package main.java;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import main.java.agent.CustomAgent;
import main.java.agent.CustomMiddleAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;

public class SocNetAgent extends CustomMiddleAgent {

	private Parent rootSceneNode;
	private RootController agentSceneController;

	public SocNetAgent(CustomAgent parent, Stage primaryStage) {
		super(parent, AgentID.SOCNET_AGENT, primaryStage);

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SocNetScene.fxml"));
			rootSceneNode = loader.load();
			agentSceneController = (RootController) loader.getController();
			// agentSceneController.setAgent(this);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public Parent getScene() {
		return rootSceneNode;
	}

	@Override
	public void receiveMessage(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String destination, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scatterMessage(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void switchAgent(AgentID destinationAgent) {
		// TODO Auto-generated method stub

	}

}
