package main.java.abstraction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginSceneComponents {

    @FXML
    protected Label userIDLabel;

    @FXML
    protected TextField userIDField;

    @FXML
    protected Label passwordLabel;

    @FXML
    protected PasswordField passwordField;

    @FXML
    protected Button loginButton;

    @FXML
    protected Button registerButton;

    @FXML
    protected Text resultText;
}
