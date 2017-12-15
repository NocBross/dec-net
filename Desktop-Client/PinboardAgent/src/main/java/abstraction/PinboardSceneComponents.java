package main.java.abstraction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;

public class PinboardSceneComponents {

    @FXML
    protected BorderPane borderPane;

    @FXML
    protected Button addPostButton;

    @FXML
    protected ScrollPane scrollPane;

    @FXML
    protected ListView<TitledPane> postListView;

}
