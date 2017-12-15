package test.java.message;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import main.java.message.LoginMessage;
import main.java.message.PostMessage;

public class PostMessageTest {

    @Test
    public void test() {
        int numberOfPosts = 5;
        String postPrefix = "post-";
        List<String> posts = new LinkedList<String>();
        PostMessage message = new PostMessage();

        // test post methods
        Assert.assertEquals(0, message.getPosts().size());
        for (int i = 0; i < numberOfPosts; i++) {
            posts.add(postPrefix + (i + 1));
        }
        message.setPostList(posts);
        posts = message.getPosts();
        for (int i = 0; i < posts.size(); i++) {
            Assert.assertEquals(postPrefix + (i + 1), posts.get(i));
        }

        // test update profile methods
        Assert.assertFalse(message.getUpdateProfile());
        message.setUpdateProfile(true);
        Assert.assertTrue(message.getUpdateProfile());
        message.setUpdateProfile(false);
        Assert.assertFalse(message.getUpdateProfile());

        // test convert method
        String testString = null;
        Assert.assertNull(PostMessage.parse(testString));
        testString = "test";
        Assert.assertNull(PostMessage.parse(testString));
        LoginMessage loginMessage = new LoginMessage();
        Assert.assertNull(PostMessage.parse(loginMessage.getMessage()));
        Assert.assertEquals(message.getMessage(), PostMessage.parse(message.getMessage()).getMessage());
    }

}
