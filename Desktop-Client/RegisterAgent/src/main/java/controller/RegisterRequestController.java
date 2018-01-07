package main.java.controller;

import main.java.abstraction.RegisterData;
import main.java.agent.CustomAgent;
import main.java.agent.RequestController;
import main.java.constants.Network;
import main.java.constants.WebServiceContext;
import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;
import main.java.message.UserIDMessage;
import main.java.service.CustomLogger;

public class RegisterRequestController implements RequestController {

    private String logID;
    private CustomAgent agent;
    private CustomLogger logger;
    private RegisterData data;
    private RegisterValidationController validationController;


    // constructor
    public RegisterRequestController(RegisterData data, RegisterValidationController validationController) {
        logID = "RegisterRequestController";
        this.data = data;
        this.validationController = validationController;
    }


    @Override
    public int receiveResult(String message) {
        logger.writeLog(logID + " received message " + message, null);
        ReportMessage answer = ReportMessage.parse(message);
        if(answer != null && answer.getReferencedMessage().equals(RegisterMessage.ID)) {
            return answer.getStatusCode();
        } else {
            return 1;
        }
    }


    @Override
    public int sendRequest() {
        if( !validationController.validateUserID()) {
            return 1;
        }

        if( !validationController.validatePassword()) {
            return 2;
        }

        if( !validationController.checkForPasswordEquality()) {
            return 3;
        }

        String url = Network.NETWORK_PROTOCOL + data.getResourceHubAddress() + ":" + Network.SERVER_WEBSERVICE_PORT
                + WebServiceContext.REGISTER;
        RegisterMessage message = new RegisterMessage();
        UserIDMessage userIDMessage = new UserIDMessage();
        message.setUserID(data.getUserID());
        userIDMessage.setUserID(data.getUserID());
        message.setPassword(data.getPassword());

        logger.writeLog(logID + " sending register request to " + url, null);

        agent.sendMessage(url, message);
        agent.scatterMessage(userIDMessage.getMessage());

        return 0;
    }


    @Override
    public void setAgent(CustomAgent newAgent) {
        agent = newAgent;
    }


    @Override
    public void setLogger(CustomLogger newLogger) {
        logger = newLogger;
    }
}
