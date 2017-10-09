package main.java;

import main.java.agent.CustomAgent;
import main.java.agent.CustomBottomAgent;
import main.java.constants.AgentID;

public class FriendshipAgent extends CustomBottomAgent {

	public FriendshipAgent(CustomAgent parent) {
		super(parent, AgentID.FRIENDSHIP_AGENT);
	}

}
