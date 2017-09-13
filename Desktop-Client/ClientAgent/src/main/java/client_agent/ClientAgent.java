package main.java.client_agent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.AuthenticationAgent;
import main.java.agent.LTAgent;
import main.java.client_agent.abstraction.ConnectionModel;
import main.java.client_agent.abstraction.TransmitModel;
import main.java.client_agent.abstraction.UserModel;
import main.java.client_agent.controller.ReceiveController;
import main.java.client_agent.controller.TransmitController;
import main.java.constants.EndPoint;
import main.java.constants.LTAgentID;
import main.java.message.LoginMessage;
import main.java.message.LogoutMessage;
import main.java.message.ReportMessage;

/**
 * The ClientAgent is the root of the agent tree structure.<br>
 * He defines the way to access the database and stores all connections.
 * 
 * @author developer
 *
 */

public class ClientAgent extends LTAgent {

	private Lock messageLock;
	private LTAgent activeAgent;
	private StringProperty newMessage;

	private ConnectionModel connectionModel;
	private UserModel userModel;
	private TransmitModel transmitModel;

	private ReceiveController receiveController;
	private TransmitController transmitController;

	public ClientAgent() throws Exception {
		super(null, LTAgentID.CLIENT_AGENT);

		addChild(new AuthenticationAgent(this));

		messageLock = new ReentrantLock();
		activeAgent = children.get(0);
		newMessage = new SimpleStringProperty();
		newMessage.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				messageLock.lock();
				if (newValue != null) {
					receiveMessage(newValue);
				}
				messageLock.unlock();
			} // end of changed

		});

		createModels();
		createController();
		startController();
	}

	public void close() {
		try {
			LogoutMessage message = new LogoutMessage();
			message.setSender(userModel.getNickname());

			transmitController.addMessage(EndPoint.SHIPPING_END_POINT, message.getMessage());
			transmitController.stopController();
			transmitController.join();

			System.exit(0);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	@Override
	public Scene getScene() {
		return activeAgent.getScene();
	}

	@Override
	public void receiveMessage(String message) {
		handleReportMessage(message);
		scatterMessage(message);

		newMessage.set(null);
	}

	@Override
	public void scatterMessage(String message) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).receiveMessage(message);
		}
	}

	@Override
	public void sendMessage(String destination, String message) {
		transmitController.addMessage(destination, message);
		handleLoginMessage(message);
	}

	@Override
	public void switchAgent(LTAgentID destinationAgent) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < children.size(); i++) {
					if (children.get(i).getID() == destinationAgent) {
						Stage stage = (Stage) activeAgent.getScene().getWindow();
						stage.setScene(children.get(i).getScene());
						activeAgent = children.get(i);
						break;
					}
				}
			}

		});
	}

	/**
	 * Changes the message in the string property.
	 * 
	 * @param newMessage
	 *            - new received message
	 */
	public void updateMessage(String newMessage) {
		messageLock.lock();
		this.newMessage.setValue(newMessage);
		messageLock.unlock();
	}

	/**
	 * Creates instances of all necessary controllers for the client agent.
	 * 
	 * @throws UnknownHostException
	 *             - if no IP address for the host could be found, or if a scope_id
	 *             was specified for a global IPv6 address.
	 */
	private void createController() throws UnknownHostException {
		receiveController = new ReceiveController(connectionModel, this);
		transmitController = new TransmitController(connectionModel, transmitModel);
	}

	/**
	 * Creates instances of all necessary models for the client agent.
	 * 
	 * @throws UnknownHostException
	 */
	private void createModels() throws Exception {
		connectionModel = new ConnectionModel(InetAddress.getByName("localhost"));
		userModel = new UserModel();
		transmitModel = new TransmitModel();
	}

	/**
	 * Stored the mail address and password from an outgoing login message in the
	 * local model.
	 * 
	 * @param message
	 *            - login message
	 */
	private void handleLoginMessage(String message) {
		LoginMessage loginMessage = LoginMessage.parse(message);
		if (loginMessage != null) {
			userModel.setNickname(loginMessage.getNickname());
			userModel.setPassword(loginMessage.getPassword());
		}
	}

	/**
	 * Handles an incoming report message.
	 * 
	 * @param message
	 *            - report message
	 */
	private void handleReportMessage(String message) {
		ReportMessage reportMessage = ReportMessage.parse(message);
		if (reportMessage != null) {
			if (reportMessage.getReferencedMessage().equals(LoginMessage.ID) && reportMessage.getResult()) {
			} else {
				receiveController.stopController();

				connectionModel.lockServerConnection();
				connectionModel.deleteServerConnection();
				receiveController = new ReceiveController(connectionModel, this);
				receiveController.start();
				connectionModel.unlockServerConnection();
			}
		}
	}

	/**
	 * Starts all necessary controllers.
	 */
	private void startController() {
		receiveController.start();
		transmitController.start();
	}
}
