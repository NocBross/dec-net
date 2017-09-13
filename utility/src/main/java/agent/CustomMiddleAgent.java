package main.java.agent;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.constants.LTAgentID;

public class CustomMiddleAgent extends LTAgent {

	protected LTAgent activeAgent;

	public CustomMiddleAgent(LTAgent parent, LTAgentID ID) {
		super(parent, ID);
	}

	@Override
	public Scene getScene() {
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
	public void sendMessage(String destination, String message) {
		parent.sendMessage(destination, message);
	}

	@Override
	public void switchAgent(LTAgentID destinationAgent) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				int index = 0;

				for (index = 0; index < children.size(); index++) {
					if (children.get(index).getID() == destinationAgent) {
						Stage stage = (Stage) activeAgent.getScene().getWindow();
						stage.setScene(children.get(index).getScene());
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
