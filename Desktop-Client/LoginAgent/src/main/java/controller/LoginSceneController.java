package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.java.abstraction.LoginData;
import main.java.abstraction.LoginSceneComponents;
import main.java.agent.LTAgent;
import main.java.agent.RootController;
import main.java.constants.LTAgentID;
import main.java.message.RegisterMessage;

/**
 * This SceneController defines the behavior of the login scene.
 * 
 * @author developer
 *
 */

public class LoginSceneController extends LoginSceneComponents implements RootController {

    private LTAgent agent;
    private LoginData data;
    private LoginRequestController requestController;

    @FXML
    public void initialize() {
        agent = null;
        data = new LoginData();
        requestController = new LoginRequestController(data);

        data.getNicknameProperty().bind(nicknameField.textProperty());
        data.getPasswordProperty().bind(passwordField.textProperty());
    }

    @Override
    public void receiveResult(String message) {
        handleRegisterMessage(message);
        handleReportMessage(message);
    }

    @Override
    public void setAgent(LTAgent newAgent) {
        agent = newAgent;
        requestController.setAgent(newAgent);
    }

    /**
     * Opens the register scene when the register text is clicked.
     * 
     * @param event
     *            - mouse released event
     */
    @FXML
    protected void registerButtonAction(ActionEvent event) {
        registerButton.setCursor(Cursor.DEFAULT);
        agent.switchAgent(LTAgentID.REGISTER_AGENT);
    }

    /**
     * Sets the courser to a hand when the mouse is over the register text.
     * 
     * @param event
     *            - mouse entered event
     */
    @FXML
    protected void hover(MouseEvent event) {
        if (event.getSource() instanceof Text) {
            ((Text) event.getSource()).setCursor(Cursor.HAND);
        }
    }

    /**
     * Sets the courser to the default courser when the register text is leaved.
     * 
     * @param event
     *            - mouse exited event
     */
    @FXML
    protected void noHover(MouseEvent event) {
        if (event.getSource() instanceof Text) {
            ((Text) event.getSource()).setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Sends the user data to the server to validate them and change to the personal
     * wall by success.
     * 
     * @param event
     *            - action event
     */
    @FXML
    protected void submitButtonAction(ActionEvent event) {
        disableControlElements();
        if (requestController.sendRequest() == 1) {
            resultText.setText("Benutzername oder Passwort falsch!");
            enableControlElements();
        }
    }

    /**
     * Sends the user data to the server to validate them and change to the personal
     * wall by success when the password field is focused.
     * 
     * @param event
     *            - key released event
     */
    @FXML
    protected void submitKeyEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            disableControlElements();
            if (requestController.sendRequest() == 1) {
                resultText.setText("Benutzername oder Passwort falsch!");
                enableControlElements();
            }
        }
    }

    /**
     * Disables all control elements of the scene.
     */
    private void disableControlElements() {
        loginButton.setDisable(true);
        registerButton.setDisable(true);
    }

    /**
     * Enables all control elements of the scene.
     */
    private void enableControlElements() {
        loginButton.setDisable(false);
        registerButton.setDisable(false);
    }

    /**
     * Handles an incoming RegisterMessage.
     * 
     * @param message
     *            - incoming message
     */
    private void handleRegisterMessage(String message) {
        RegisterMessage registerMessage = RegisterMessage.parse(message);

        if (registerMessage != null) {
            nicknameField.setText(registerMessage.getNickname());
        }
    }

    /**
     * Handles an incoming report message.
     * 
     * @param message
     *            - incoming message
     */
    private void handleReportMessage(String message) {
        switch (requestController.receiveResult(message)) {
            case 0:
                nicknameField.setText("");
                passwordField.setText("");
                resultText.setText("Login erfolgreich!");
                enableControlElements();
                break;
            case 1:
                break;
            case 2:
                resultText.setText("Benutzername oder Passwort falsch!");
                enableControlElements();
                break;
        }
    }

}
