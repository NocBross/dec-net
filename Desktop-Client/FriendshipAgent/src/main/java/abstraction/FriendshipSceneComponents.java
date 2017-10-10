package main.java.abstraction;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;

public class FriendshipSceneComponents {

    @FXML
    protected ScrollPane scrollPane;

    @FXML
    protected Label searchLabel;

    @FXML
    protected TitledPane directMessagePane;

    @FXML
    protected TitledPane friendPane;

    @FXML
    protected TitledPane groupPane;

    protected ListView<String> directMessageList;

}
