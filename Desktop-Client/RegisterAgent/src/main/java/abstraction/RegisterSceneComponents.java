package main.java.abstraction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RegisterSceneComponents {

	@FXML
	protected Button cancelButton;

	@FXML
	protected TextField nicknameField;

	@FXML
	protected PasswordField passwordField;

	@FXML
	protected PasswordField passwordRepeatField;

	@FXML
	protected Button registerButton;

	@FXML
	protected Text registerInfoText;

	@FXML
	protected Text specialCharacters;
}
