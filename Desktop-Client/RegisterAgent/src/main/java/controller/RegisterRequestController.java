package main.java.controller;

import main.java.abstraction.RegisterData;
import main.java.agent.CustomAgent;
import main.java.agent.RequestController;
import main.java.constants.WebServiceContext;
import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;
import main.java.message.UserIDMessage;

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
            return answer.getStatusCode();
        } else {
            return 1;
        }
    }

    @Override
    public int sendRequest() {
        if (!validationController.validateUserID()) {
            return 1;
        }

        if (!validationController.validatePassword()) {
            return 2;
        }

        if (!validationController.checkForPasswordEquality()) {
            return 3;
        }

        RegisterMessage message = new RegisterMessage();
        UserIDMessage userIDMessage = new UserIDMessage();
        message.setUserID(data.getUserID());
        userIDMessage.setUserID(data.getUserID());
        message.setPassword(data.getPassword());
        agent.sendMessage(data.getResourceHubAddress(), WebServiceContext.REGISTER, message.getMessage());
        agent.scatterMessage(userIDMessage.getMessage());

        return 0;
    }

    @Override
    public void setAgent(CustomAgent newAgent) {
        agent = newAgent;
    }
}
