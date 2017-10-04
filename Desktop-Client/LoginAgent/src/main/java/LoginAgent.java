package main.java;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import main.java.agent.CustomBottomAgent;
import main.java.agent.CustomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;

public class LoginAgent extends CustomBottomAgent {

	public LoginAgent(CustomAgent parent) {
		super(parent, AgentID.LOGIN_AGENT);

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/LoginScene.fxml"));
			rootSceneNode = loader.load();
			agentSceneController = (RootController) loader.getController();
			agentSceneController.setAgent(this);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
