package main.java.controller;

import main.java.abstraction.LoginData;
import main.java.agent.LTAgent;
import main.java.agent.RequestController;
import main.java.constants.EndPoint;
import main.java.message.LoginMessage;
import main.java.message.ReportMessage;

/**
 * The LoginRequestController generates the LoginMessage and handles the
 * incoming result.
 * 
 * @author developer
 *
 */

public class LoginRequestController implements RequestController {

	private LTAgent agent;
	private LoginData data;
	private LoginValidationController validationController;

	public LoginRequestController(LoginData data) {
		agent = null;
		this.data = data;
		validationController = new LoginValidationController(data);
	}

	/**
	 * Checks if the given message is the result for the request.
	 * 
	 * @param message
	 *            - incoming result message
	 * @return 0, in case of success<br>
	 *         1, in case of the incoming message was not a result<br>
	 *         2, in case of wrong mail of password
	 */
	@Override
	public int receiveResult(String message) {
		ReportMessage answer = ReportMessage.parse(message);

		if (answer != null && answer.getReferencedMessage().equals(LoginMessage.ID)) {
			if (answer.getResult()) {
				return 0;
			} else {
				return 2;
			}
		}

		return 1;
	}

	/**
	 * Validates the data by using the ValidationController, generates the
	 * LoignMessage and send the message.
	 * 
	 * @return 0, in case of success<br>
	 *         1, in case of empty nickname or invalid password
	 */
	@Override
	public int sendRequest() {
		if (!validationController.validateNickname() || !validationController.validatePassword()) {
			return 1;
		}

		LoginMessage message = new LoginMessage();
		message.setNickname(data.getNickname());
		message.setPassword(data.getPassword());
		agent.sendMessage(EndPoint.LOGIN_END_POINT, message.getMessage());
		agent.scatterAllMessage(message.getMessage());

		return 0;
	}

	/**
	 * Sets a new agent to the controller.
	 * 
	 * @param newAgent
	 *            - new agent of the controller
	 */
	@Override
	public void setAgent(LTAgent newAgent) {
		this.agent = newAgent;
	}
}
