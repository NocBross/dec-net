package main.java.controller;

import java.util.List;

import main.java.constants.PostConstants;

public class PostSortController {

    public static int getPostID(String post) {
        return Integer.valueOf(post.substring(post.lastIndexOf(PostConstants.POST_NUMBER_SEPARATOR) + 1));
    }

    /**
     * Sorts the entries in the list in descending order.
     * 
     * @param postList
     */
    public static void sort(List<String> postList) {
        int tempID = -1;
        String temp = null;
        for (int i = 1; i < postList.size(); i++) {
            temp = postList.get(i);
            tempID = getPostID(temp);
            int j = i;
            while (j > 0 && getPostID(postList.get(j - 1)) < tempID) {
                postList.set(j, postList.get(j - 1));
                j--;
            }
            postList.set(j, temp);
        }
    }
}
