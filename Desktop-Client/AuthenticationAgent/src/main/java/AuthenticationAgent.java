package main.java;

import main.java.agent.CustomMiddleAgent;
import main.java.agent.LTAgent;
import main.java.constants.LTAgentID;

/**
 * The AthenticationAgent knows all agents which are used for the login and
 * registration process.
 * 
 * @author developer
 *
 */

public class AuthenticationAgent extends CustomMiddleAgent {

	public AuthenticationAgent(LTAgent parent) {
		super(parent, LTAgentID.AUTHENTICATION_AGENT);

		addChild(new LoginAgent(this));
		addChild(new RegisterAgent(this));

		activeAgent = children.get(0);
	}
}
