package main.java.abstraction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import main.java.constants.Network;
import main.java.constants.PostConstants;
import main.java.controller.PostSortController;
import main.java.rdf.PostRDF;

public class Posts {

    private static Posts posts = null;

    private int maxPostID;
    private int emptyCells;
    private String userID;
    private Map<String, PostRDF> postMap;
    private ObservableList<TitledPane> postList;

    private Posts() {
        emptyCells = 0;
        maxPostID = 0;
        postMap = new HashMap<String, PostRDF>();
        postList = FXCollections.observableArrayList();
    }

    public static Posts getInstance() {
        if (posts == null) {
            posts = new Posts();
        }

        return posts;
    }

    public String addNewPost(String content) {
        maxPostID++;
        String postID = PostConstants.POST_PREFIX + maxPostID;
        String postURL = Network.NETWORK_PROTOCOL + userID.split("@")[1] + "/" + userID.split("@")[0] + "/post/"
                + PostConstants.POST_PREFIX + maxPostID;

        TitledPane pane = new TitledPane();
        pane.setExpanded(true);
        pane.setText(postID);
        pane.setContent(new Text(content));
        postList.add(0, pane);

        PostRDF post = new PostRDF();
        post.setCreator(userID);
        post.setContent(content);
        postMap.put(postURL, post);

        return postURL;
    }

    public void addPost(String postURL, String serializedPost) {
        int ID = Integer.valueOf(postURL.substring(postURL.lastIndexOf(PostConstants.POST_NUMBER_SEPARATOR) + 1));
        maxPostID = Math.max(maxPostID, ID);

        if (postMap.containsKey(postURL)) {
            PostRDF post = new PostRDF();
            post.setModel(serializedPost);
            postMap.put(postURL, post);
            emptyCells--;
        }

        if (emptyCells == 0) {
            List<TitledPane> userPostPanes = new LinkedList<TitledPane>();
            List<String> userPosts = new LinkedList<String>();
            Iterator<String> iterator = postMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (PostSortController.getPostID(key) > 0) {
                    userPosts.add(key);
                }
            }
            PostSortController.sort(userPosts);

            for (int i = 0; i < userPosts.size(); i++) {
                String postID = userPosts.get(i).substring(userPosts.get(i).lastIndexOf("/") + 1);
                TitledPane pane = new TitledPane();
                pane.setExpanded(false);
                pane.setText(postID);
                pane.setContent(new Text(postMap.get(userPosts.get(i)).getContent()));
                userPostPanes.add(pane);
            }

            postList.removeAll(userPostPanes);
            postList.addAll(userPostPanes);
            postList.get(0).setExpanded(true);
        }
    }

    public ObservableList<TitledPane> getPostList() {
        return postList;
    }

    public void setPostMap(List<String> postList) {
        postMap = new HashMap<String, PostRDF>();
        PostSortController.sort(postList);
        for (int i = 0; i < postList.size(); i++) {
            postMap.put(postList.get(i), null);
        }
        if (postList.size() > 0) {
            maxPostID = PostSortController.getPostID(postList.get(0));
            emptyCells = postList.size();
        }
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
