package main.java.abstraction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RegisterSceneComponents {

    @FXML
    protected Text registerHeader;

    @FXML
    protected Label registerNicknameLabel;

    @FXML
    protected TextField nicknameField;

    @FXML
    protected Label resourceHubAddressLabel;

    @FXML
    protected TextField resourceHubAddressField;

    @FXML
    protected Label userIDLabel;

    @FXML
    protected TextField userIDField;

    @FXML
    protected Label passwordLabel;

    @FXML
    protected PasswordField passwordField;

    @FXML
    protected Label repeatPasswordLabel;

    @FXML
    protected PasswordField passwordRepeatField;

    @FXML
    protected Text registerInfoText;

    @FXML
    protected Text tipHeaderText;

    @FXML
    protected Text userIDTipText;

    @FXML
    protected Text passwordTipText;

    @FXML
    protected Text specialCharacters;

    @FXML
    protected Button registerButton;

    @FXML
    protected Button cancelButton;
}
