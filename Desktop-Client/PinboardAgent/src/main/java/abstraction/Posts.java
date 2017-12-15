package main.java.abstraction;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import main.java.constants.Network;
import main.java.constants.PostConstants;
import main.java.controller.PostSortController;

public class Posts {

    private static Posts posts = null;

    private int maxPostID;
    private String userID;
    private ObservableList<TitledPane> postList;

    private Posts() {
        maxPostID = 1;
        postList = FXCollections.observableArrayList();
    }

    public static Posts getInstance() {
        if (posts == null) {
            posts = new Posts();
        }

        return posts;
    }

    public void addNewPost(String post) {
        maxPostID++;
        String postID = PostConstants.POST_PREFIX + maxPostID;
        String postURL = Network.NETWORK_PROTOCOL + userID.split("@")[1] + "/" + userID.split("@")[0] + "/post/"
                + PostConstants.POST_PREFIX + maxPostID;
        TitledPane pane = new TitledPane();
        pane.setExpanded(true);
        pane.setText(postID);
        pane.setContent(new Text(post + ": " + postURL));

        postList.add(0, pane);
    }

    public void addPosts(List<String> newPosts) {
        if (newPosts.size() > 0) {
            PostSortController.sort(newPosts);
            maxPostID = Integer.valueOf(
                    newPosts.get(0).substring(newPosts.get(0).lastIndexOf(PostConstants.POST_NUMBER_SEPARATOR) + 1));

            for (int i = 0; i < newPosts.size() && PostSortController.getPostID(newPosts.get(i)) > 0; i++) {
                String postID = newPosts.get(i).substring(newPosts.get(i).lastIndexOf("/") + 1);
                TitledPane pane = new TitledPane();
                pane.setExpanded(false);
                pane.setText(postID);
                pane.setContent(new Text(newPosts.get(i)));
                postList.add(pane);
            }

            postList.get(0).setExpanded(true);
        }
    }

    public ObservableList<TitledPane> getPostList() {
        return postList;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
