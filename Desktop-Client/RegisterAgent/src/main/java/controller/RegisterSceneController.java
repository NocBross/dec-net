package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import main.java.abstraction.Language;
import main.java.abstraction.RegisterData;
import main.java.abstraction.RegisterSceneComponents;
import main.java.agent.CustomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;
import main.java.constants.ServerStatusCodes;

/**
 * The RegisterSceneController defines the behavior of the register scene.
 * 
 * @author developer
 *
 */

public class RegisterSceneController extends RegisterSceneComponents implements RootController {

    private CustomAgent agent;
    private RegisterData data;
    private RegisterValidationController validationController;
    private RegisterRequestController requestController;

    @FXML
    public void initialize() {
        setGUIStrings();
        agent = null;
        data = new RegisterData();
        validationController = new RegisterValidationController(data);
        requestController = new RegisterRequestController(data, validationController);

        data.getNicknameProperty().bind(nicknameField.textProperty());
        data.getResourceHubAddressProperty().bind(resourceHubAddressField.textProperty());
        userIDField.textProperty().bind(data.getUserIDProperty());
        data.getPasswordProperty().bind(passwordField.textProperty());
        data.getRepeatedPasswordProperty().bind(passwordRepeatField.textProperty());
    }

    @Override
    public void receiveResult(String message) {
        switch (requestController.receiveResult(message)) {
            case ServerStatusCodes.REGISTER_CORRECT:
                registerButton.setDisable(false);
                cancelButton.setDisable(false);
                changeToAgent(AgentID.LOGIN_AGENT);
                break;
            case ServerStatusCodes.REGISTER_KNOWN_USER_ID:
                registerButton.setDisable(false);
                cancelButton.setDisable(false);
                registerInfoText.setText(Language.REGISTER_ERROR_EXISTING_NICKNAME);
                break;
            default:
                break;
        }
    }

    @Override
    public void setAgent(CustomAgent newAgent) {
        agent = newAgent;
        requestController.setAgent(newAgent);
    }

    /**
     * Clears the text fields and go back to the login scene.
     * 
     * @param event
     *            - action event
     */
    @FXML
    protected void cancelButtonAction(ActionEvent event) {
        changeToAgent(AgentID.LOGIN_AGENT);
    }

    /**
     * Checks if the value in the password repeat field is the same as the value in
     * the password filed while the user is writing.
     * 
     * @param event
     *            - key released
     */
    @FXML
    protected void passwordMatchingCheck(KeyEvent event) {
        if (validationController.checkForPasswordEquality()) {
            registerInfoText.setText("");
        } else {
            registerInfoText.setText(Language.REGISTER_ERROR_PASSWORD_DO_NOT_MATCH);
        }
    }

    /**
     * Checks if the values in the different text fields are valid, creates the
     * personal key and sends a request to the server.<br>
     * After a positive answer from the server the method will clear the different
     * text fields and go back to the login scene.
     * 
     * @param event
     *            - action event
     */
    @FXML
    protected void registerButtonAction(ActionEvent event) {
        switch (requestController.sendRequest()) {
            case 0:
                registerButton.setDisable(true);
                cancelButton.setDisable(true);
                break;
            case 1:
                registerInfoText.setText(Language.REGISTER_ERROR_WRONG_NICKNAME);
                break;
            case 2:
                registerInfoText.setText(Language.REGISTER_ERROR_PASSWORD_NOT_SAVE);
                break;
            case 3:
                registerInfoText.setText(Language.REGISTER_ERROR_PASSWORD_DO_NOT_MATCH);
                break;
        }
    }

    /**
     * Deletes the text in the different text fields and changes to the login
     * service.
     */
    private void changeToAgent(AgentID destination) {
        nicknameField.setText("");
        resourceHubAddressField.setText("");
        passwordField.setText("");
        passwordRepeatField.setText("");
        registerInfoText.setText("");

        agent.switchAgent(destination);
    }

    private void setGUIStrings() {
        registerHeader.setText(Language.REGISTER_HEADER);
        registerNicknameLabel.setText(Language.REGISTER_NICKNAME);
        resourceHubAddressLabel.setText(Language.REGISTER_RESOURCE_HUB_ADDRESS);
        userIDLabel.setText(Language.REGISTER_USER_ID);
        passwordLabel.setText(Language.REGISTER_PASSWORD);
        repeatPasswordLabel.setText(Language.REGISTER_PASSWORD_REPEAT);
        registerInfoText.setText("");
        tipHeaderText.setText(Language.REGISTER_TIP_HEADER);
        userIDTipText.setText(Language.REGISTER_USER_ID_TIP);
        passwordTipText.setText(Language.REGISTER_PASSWORD_TIP);
        specialCharacters.setText(Language.SPECIAL_CHARACTERS);
        registerButton.setText(Language.REGISTER_BUTTON_REGISTER);
        cancelButton.setText(Language.REGISTER_BUTTON_CANCEL);
    }

}
