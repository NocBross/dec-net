package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import main.java.abstraction.RegisterData;
import main.java.abstraction.RegisterSceneComponents;
import main.java.agent.LTAgent;
import main.java.agent.RootController;
import main.java.constants.LTAgentID;

/**
 * The RegisterSceneController defines the behavior of the register scene.
 * 
 * @author developer
 *
 */

public class RegisterSceneController extends RegisterSceneComponents implements RootController {

	private LTAgent agent;
	private RegisterData data;
	private RegisterValidationController validationController;
	private RegisterRequestController requestController;

	@FXML
	public void initialize() {
		specialCharacters.setText("mögliche Sonderzeichen: @#$%^&+=!?;<>_.");
		agent = null;
		data = new RegisterData();
		validationController = new RegisterValidationController(data);
		requestController = new RegisterRequestController(data, validationController);

		data.getNicknameProperty().bind(nicknameField.textProperty());
		data.getPasswordProperty().bind(passwordField.textProperty());
		data.getRepeatedPasswordProperty().bind(passwordRepeatField.textProperty());
	}

	@Override
	public void receiveResult(String message) {
		switch (requestController.receiveResult(message)) {
		case 0:
			registerButton.setDisable(false);
			cancelButton.setDisable(false);
			changeToAgent(LTAgentID.LOGIN_AGENT);
			break;
		case 1:
			break;
		case 2:
			registerButton.setDisable(false);
			cancelButton.setDisable(false);
			registerInfoText.setText("Nutzername bereits vorhanden!");
			break;
		}
	}

	@Override
	public void setAgent(LTAgent newAgent) {
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
		changeToAgent(LTAgentID.LOGIN_AGENT);
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
			registerInfoText.setText("Passwörter stimmen nicht überein!");
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
			registerInfoText.setText("Bitte geben Sie Ihren gewünschten Benutzernamen ein.");
			break;
		case 2:
			registerInfoText.setText("Passwort entspricht nicht den Richtlinien!");
			break;
		case 3:
			registerInfoText.setText("Passwörter stimmen nicht überein!");
			break;
		}
	}

	/**
	 * Deletes the text in the different text fields and changes to the login
	 * service.
	 */
	private void changeToAgent(LTAgentID destination) {
		nicknameField.setText("");
		passwordField.setText("");
		passwordRepeatField.setText("");
		registerInfoText.setText("");

		agent.switchAgent(destination);
	}

}
