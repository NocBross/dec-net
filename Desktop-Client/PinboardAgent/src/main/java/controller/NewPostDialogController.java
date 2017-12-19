package main.java.controller;

import java.util.LinkedList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.PinboardAgent;
import main.java.abstraction.NewPostDialogComponents;
import main.java.abstraction.Posts;
import main.java.constants.Network;
import main.java.message.PostMessage;
import main.java.message.RDFMessage;
import main.java.rdf.PostRDF;

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
            String postURL = Posts.getInstance().addNewPost(postTextArea.getText());
            generatePostRDF(postURL, postTextArea.getText());

            List<String> postList = new LinkedList<String>();
            postList.add(postURL);
            PostMessage postMessage = new PostMessage();
            postMessage.setPostList(postList);
            postMessage.setUpdateProfile(true);
            agent.scatterMessage(postMessage.getMessage());
        }
        close();
    }

    private void generatePostRDF(String postID, String content) {
        PostRDF postModel = new PostRDF();

        postModel.setCreator(Posts.getInstance().getUserID());
        postModel.setContent(content);

        RDFMessage message = new RDFMessage(postID.replace(Network.NETWORK_PROTOCOL, ""), postModel.getModel());
        String[] splittedURL = postID.replace(Network.NETWORK_PROTOCOL, "").split("/");
        String url = Network.NETWORK_PROTOCOL + splittedURL[0] + ":" + Network.SERVER_WEBSERVICE_PORT + "/"
                + splittedURL[1] + "/" + splittedURL[2] + "/" + splittedURL[3];
        agent.sendMessage(url, message);
        agent.storeRDFModel(message);
    }

}
