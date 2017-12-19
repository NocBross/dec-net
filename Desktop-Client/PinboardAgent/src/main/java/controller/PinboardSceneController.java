package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.PinboardAgent;
import main.java.abstraction.PinboardSceneComponents;
import main.java.abstraction.Posts;
import main.java.agent.CustomAgent;
import main.java.agent.RootController;
import main.java.constants.Network;
import main.java.constants.WebServiceConstants;
import main.java.message.PostMessage;
import main.java.message.RDFMessage;
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
    }

    @Override
    public void receiveResult(String message) {
        handlePostMessage(message);
        handleRDFMessage(message);
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
            if (!postMessage.getUpdateProfile()) {
                posts.setPostMap(postMessage.getPosts());
                for (int i = 0; i < postMessage.getPosts().size(); i++) {
                    String[] splittedURL = postMessage.getPosts().get(i).replace(Network.NETWORK_PROTOCOL, "")
                            .split("/");
                    String url = Network.NETWORK_PROTOCOL + splittedURL[0] + ":" + Network.SERVER_WEBSERVICE_PORT + "/"
                            + splittedURL[1] + "/" + splittedURL[2] + "/" + splittedURL[3];
                    agent.sendMessage(url, null);
                }
            }
        }
    }

    private void handleRDFMessage(String message) {
        RDFMessage rdfMessage = RDFMessage.parse(message);
        if (rdfMessage != null) {
            if (rdfMessage.getResourceID().indexOf(WebServiceConstants.POST_RESOURCE) != -1) {
                posts.addPost(Network.NETWORK_PROTOCOL + rdfMessage.getResourceID(), rdfMessage.getModel());
            }
        }
    }

    private void handleUserIDMessage(String message) {
        UserIDMessage userIDMessage = UserIDMessage.parse(message);
        if (userIDMessage != null) {
            posts.setUserID(userIDMessage.getUserID());
        }
    }

}
