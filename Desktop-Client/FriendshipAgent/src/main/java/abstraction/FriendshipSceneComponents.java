package main.java.abstraction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;

public class FriendshipSceneComponents {

    @FXML
    protected ScrollPane scrollPane;

    @FXML
    protected TitledPane friendPane;

    @FXML
    protected Button addNewFriendButton;

    @FXML
    protected ListView<String> friendList;

    @FXML
    protected TitledPane groupPane;

    @FXML
    protected Button addNewGroupButton;

    @FXML
    protected ListView<String> groupList;

}
