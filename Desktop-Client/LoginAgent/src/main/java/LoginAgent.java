package main.java;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import main.java.agent.CustomBottomAgent;
import main.java.agent.LTAgent;
import main.java.agent.RootController;
import main.java.constants.LTAgentID;

public class LoginAgent extends CustomBottomAgent {

	public LoginAgent(LTAgent parent) {
		super(parent, LTAgentID.LOGIN_AGENT);

		try {
			Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/LoginScene.fxml"));
			rootAgent = loader.load();
			agentScene = new Scene(rootAgent, screenSize.getWidth(), screenSize.getHeight());
			agentSceneController = (RootController) loader.getController();
			agentSceneController.setAgent(this);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
