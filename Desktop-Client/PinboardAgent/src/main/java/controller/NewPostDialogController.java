package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.PinboardAgent;
import main.java.abstraction.NewPostDialogComponents;
import main.java.abstraction.Posts;

public class NewPostDialogController extends NewPostDialogComponents {

    private PinboardAgent agent;

    public void close() {
        agent.closeNewPostDialog();
        postTextArea.setText("");
    }

    public void setAgent(PinboardAgent newAgent) {
        agent = newAgent;
    }

    @FXML
    protected void cancelButtonAction(ActionEvent event) {
        close();
    }

    @FXML
    protected void postButtonAction(ActionEvent event) {
        if (!postTextArea.getText().equals("")) {
            Posts.getInstance().addNewPost(postTextArea.getText());
        }
        close();
    }

}
