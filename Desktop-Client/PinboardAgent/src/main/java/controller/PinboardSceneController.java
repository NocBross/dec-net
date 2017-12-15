package main.java.controller;

import java.util.LinkedList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.PinboardAgent;
import main.java.abstraction.PinboardSceneComponents;
import main.java.abstraction.Posts;
import main.java.agent.CustomAgent;
import main.java.agent.RootController;
import main.java.constants.PostConstants;
import main.java.message.PostMessage;
import main.java.message.UserIDMessage;

public class PinboardSceneController extends PinboardSceneComponents implements RootController {

    private PinboardAgent agent;
    private Posts posts;

    @FXML
    public void initialize() {
        agent = null;
        posts = Posts.getInstance();
        postListView.setItems(posts.getPostList());
        postListView.prefHeightProperty().bind(scrollPane.heightProperty());

        int numberOfPosts = 20;
        String postPrefix = "http://localhost/user/post/" + PostConstants.POST_PREFIX;
        List<String> postList = new LinkedList<String>();
        for (int i = 0; i < numberOfPosts; i++) {
            postList.add(postPrefix + (20 - i));
        }
        postList.add(postPrefix + "-1");
        posts.addPosts(postList);
    }

    @Override
    public void receiveResult(String message) {
        handlePostMessage(message);
        handleUserIDMessage(message);
    }

    @Override
    public void setAgent(CustomAgent newAgent) {
        agent = (PinboardAgent) newAgent;
    }

    @FXML
    protected void newPostButtonAction(ActionEvent event) {
        agent.showNewPostDialog();
    }

    private void handlePostMessage(String message) {
        PostMessage postMessage = PostMessage.parse(message);
        if (postMessage != null) {
            posts.addPosts(postMessage.getPosts());
        }
    }

    private void handleUserIDMessage(String message) {
        UserIDMessage userIDMessage = UserIDMessage.parse(message);
        if (userIDMessage != null) {
            posts.setUserID(userIDMessage.getUserID());
        }
    }

}
