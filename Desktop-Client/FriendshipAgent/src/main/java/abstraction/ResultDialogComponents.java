package main.java.abstraction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

public class ResultDialogComponents {

    @FXML
    protected Text resultHeaderText;

    @FXML
    protected Text nameText;

    @FXML
    protected ChoiceBox<String> nameChoiceBox;

    @FXML
    protected Button addButton;

    @FXML
    protected Button cancelButton;

}
