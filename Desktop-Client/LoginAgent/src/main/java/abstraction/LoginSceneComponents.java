package main.java.abstraction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginSceneComponents {

	@FXML
	protected Button loginButton;

	@FXML
	protected TextField nicknameField;

	@FXML
	protected PasswordField passwordField;

	@FXML
	protected Button registerButton;

	@FXML
	protected Text resetText;

	@FXML
	protected Text resultText;
}
