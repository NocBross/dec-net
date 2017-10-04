package main.java;

import javafx.stage.Stage;
import main.java.agent.CustomMiddleAgent;
import main.java.agent.CustomAgent;
import main.java.constants.AgentID;

/**
 * The AthenticationAgent knows all agents which are used for the login and
 * registration process.
 * 
 * @author developer
 *
 */

public class AuthenticationAgent extends CustomMiddleAgent {

	public AuthenticationAgent(CustomAgent parent, Stage primaryStage) {
		super(parent, AgentID.AUTHENTICATION_AGENT, primaryStage);

		addChild(new LoginAgent(this));
		addChild(new RegisterAgent(this));

		activeAgent = children.get(0);
	}
}
