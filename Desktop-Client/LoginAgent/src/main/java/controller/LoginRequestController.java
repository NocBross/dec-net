package main.java.controller;

import main.java.abstraction.LoginData;
import main.java.agent.CustomAgent;
import main.java.agent.RequestController;
import main.java.constants.Network;
import main.java.message.LoginMessage;
import main.java.message.ReportMessage;
import main.java.message.UserIDMessage;

/**
 * The LoginRequestController generates the LoginMessage and handles the
 * incoming result.
 * 
 * @author developer
 *
 */

public class LoginRequestController implements RequestController {

    private CustomAgent agent;
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
            return answer.getStatusCode();
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
        UserIDMessage userIDMessage = new UserIDMessage();
        message.setNickname(data.getNickname());
        userIDMessage.setUserID(data.getNickname());
        message.setPassword(data.getPassword());
        agent.scatterAllMessage(userIDMessage.getMessage());
        agent.sendMessage(Network.NETWORK_HUB, null, message.getMessage());

        return 0;
    }

    /**
     * Sets a new agent to the controller.
     * 
     * @param newAgent
     *            - new agent of the controller
     */
    @Override
    public void setAgent(CustomAgent newAgent) {
        this.agent = newAgent;
    }
}
