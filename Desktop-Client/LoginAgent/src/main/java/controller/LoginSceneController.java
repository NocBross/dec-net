package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.java.abstraction.Language;
import main.java.abstraction.LoginData;
import main.java.abstraction.LoginSceneComponents;
import main.java.agent.CustomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;
import main.java.constants.ServerStatusCodes;
import main.java.message.UserIDMessage;
import main.java.service.CustomLogger;

/**
 * This SceneController defines the behavior of the login scene.
 * 
 * @author developer
 *
 */

public class LoginSceneController extends LoginSceneComponents implements RootController {

    private String logID;
    private CustomAgent agent;
    private LoginData data;
    private LoginRequestController requestController;
    private CustomLogger logger;


    @FXML
    public void initialize() {
        setLanguageStrings();
        logID = "LoginSceneController";
        agent = null;
        data = new LoginData();
        requestController = new LoginRequestController(data);

        data.getNicknameProperty().bind(userIDField.textProperty());
        data.getPasswordProperty().bind(passwordField.textProperty());
    }


    @Override
    public void receiveResult(String message) {
        logger.writeLog(logID + " received message " + message, null);
        handleUserIDMessage(message);
        handleReportMessage(message);
    }


    @Override
    public void setAgent(CustomAgent newAgent) {
        agent = newAgent;
        requestController.setAgent(newAgent);
    }


    @Override
    public void setLogger(CustomLogger newLogger) {
        logger = newLogger;
        requestController.setLogger(newLogger);
    }


    /**
     * Opens the register scene when the register text is clicked.
     * 
     * @param event
     *        - mouse released event
     */
    @FXML
    protected void registerButtonAction(ActionEvent event) {
        logger.writeLog(logID + " register button clicked", null);
        registerButton.setCursor(Cursor.DEFAULT);
        agent.switchAgent(AgentID.REGISTER_AGENT);
    }


    /**
     * Sets the courser to a hand when the mouse is over the register text.
     * 
     * @param event
     *        - mouse entered event
     */
    @FXML
    protected void hover(MouseEvent event) {
        if(event.getSource() instanceof Text) {
            ((Text) event.getSource()).setCursor(Cursor.HAND);
        }
    }


    /**
     * Sets the courser to the default courser when the register text is leaved.
     * 
     * @param event
     *        - mouse exited event
     */
    @FXML
    protected void noHover(MouseEvent event) {
        if(event.getSource() instanceof Text) {
            ((Text) event.getSource()).setCursor(Cursor.DEFAULT);
        }
    }


    /**
     * Sends the user data to the server to validate them and change to the
     * personal wall by success.
     * 
     * @param event
     *        - action event
     */
    @FXML
    protected void submitButtonAction(ActionEvent event) {
        logger.writeLog(logID + " submit button clicked", null);
        disableControlElements();
        if(requestController.sendRequest() == 1) {
            resultText.setText(Language.LOGIN_ERROR_UNKNOWN_USER_ID);
            enableControlElements();
        }
    }


    /**
     * Sends the user data to the server to validate them and change to the
     * personal wall by success when the password field is focused.
     * 
     * @param event
     *        - key released event
     */
    @FXML
    protected void submitKeyEvent(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            logger.writeLog(logID + " sending login request by key event", null);
            disableControlElements();
            if(requestController.sendRequest() == 1) {
                resultText.setText(Language.LOGIN_ERROR_UNKNOWN_USER_ID);
                enableControlElements();
            }
        }
    }


    /**
     * Disables all control elements of the scene.
     */
    private void disableControlElements() {
        logger.writeLog(logID + " control elements disabled", null);
        loginButton.setDisable(true);
        registerButton.setDisable(true);
    }


    /**
     * Enables all control elements of the scene.
     */
    private void enableControlElements() {
        logger.writeLog(logID + " controll elements enabled", null);
        loginButton.setDisable(false);
        registerButton.setDisable(false);
    }


    /**
     * Handles an incoming RegisterMessage.
     * 
     * @param message
     *        - incoming message
     */
    private void handleUserIDMessage(String message) {
        UserIDMessage userIDMessage = UserIDMessage.parse(message);

        if(userIDMessage != null) {
            logger.writeLog(logID + " processing " + userIDMessage.getType(), null);
            userIDField.setText(userIDMessage.getUserID());
        }
    }


    /**
     * Handles an incoming report message.
     * 
     * @param message
     *        - incoming message
     */
    private void handleReportMessage(String message) {
        logger.writeLog(logID + " processing report message", null);
        switch(requestController.receiveResult(message)) {
            case ServerStatusCodes.LOGIN_CORRECT:
                userIDField.setText("");
                passwordField.setText("");
                resultText.setText("");
                enableControlElements();
                agent.switchAgent(AgentID.SOCNET_AGENT);
                break;
            case ServerStatusCodes.LOGIN_UNKNOWN_USER_ID:
                resultText.setText(Language.LOGIN_ERROR_UNKNOWN_USER_ID);
                enableControlElements();
                break;
            default:
                break;
        }
    }


    private void setLanguageStrings() {
        userIDLabel.setText(Language.LOGIN_USER_ID);
        passwordLabel.setText(Language.LOGIN_PASSWORD);
        loginButton.setText(Language.LOGIN_BUTTON_LOGIN);
        registerButton.setText(Language.LOGIN_BUTTON_REGISTER);
    }

}
