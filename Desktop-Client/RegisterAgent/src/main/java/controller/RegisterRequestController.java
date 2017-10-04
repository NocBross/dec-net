package main.java.controller;

import main.java.abstraction.RegisterData;
import main.java.agent.CustomAgent;
import main.java.agent.RequestController;
import main.java.constants.EndPoint;
import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;

public class RegisterRequestController implements RequestController {

	private CustomAgent agent;
	private RegisterData data;
	private RegisterValidationController validationController;

	// constructor
	public RegisterRequestController(RegisterData data, RegisterValidationController validationController) {
		this.data = data;
		this.validationController = validationController;
	}

	@Override
	public int receiveResult(String message) {
		ReportMessage answer = ReportMessage.parse(message);
		if (answer != null && answer.getReferencedMessage().equals(RegisterMessage.ID)) {
			if (answer.getResult()) {
				return 0;
			} else {
				return 2;
			}
		} else {
			return 1;
		}
	}

	@Override
	public int sendRequest() {
		if (!validationController.validateNickname()) {
			return 1;
		}

		if (!validationController.validatePassword()) {
			return 2;
		}

		if (!validationController.checkForPasswordEquality()) {
			return 3;
		}

		RegisterMessage message = new RegisterMessage();
		message.setNickname(data.getNickname());
		message.setPassword(data.getPassword());
		agent.sendMessage(EndPoint.REGISTER_END_POINT, message.getMessage());
		agent.scatterMessage(message.getMessage());

		return 0;
	}

	@Override
	public void setAgent(CustomAgent newAgent) {
		agent = newAgent;
	}
}
