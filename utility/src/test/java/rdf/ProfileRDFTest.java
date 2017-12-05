package test.java.rdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import main.java.rdf.ProfileRDF;

public class ProfileRDFTest {

    @Test
    public void test() throws Exception {
        String userID = "test_user_1@localhost";
        String friendID = "test_user_2@localhost";
        String postID = "http://localhost/test_user_1/posts/1";
        String name = "test_user_1";
        ProfileRDF profile = ProfileRDF.getInstance();
        List<String> friends = null;
        List<String> posts = null;

        // test getInstance
        Assert.assertEquals(profile, ProfileRDF.getInstance());

        // test name
        profile.setName(userID, "nick");
        Assert.assertEquals("nick", profile.getName(userID));
        profile.setName(userID, name);
        Assert.assertEquals(name, profile.getName(userID));

        // test friend methods
        profile.addFriend(userID, friendID);
        friends = profile.getFriends();
        Assert.assertNotNull(friends);
        Assert.assertEquals(1, friends.size());
        Assert.assertEquals(friendID, friends.get(0));
        profile.deleteFriend(userID, friendID);
        friends = profile.getFriends();
        Assert.assertNotNull(friends);
        Assert.assertEquals(0, friends.size());

        // test posts
        profile.addPost(userID, postID);
        posts = profile.getPosts();
        Assert.assertNotNull(posts);
        Assert.assertEquals(1, posts.size());
        Assert.assertEquals(postID, posts.get(0));
        profile.deletePost(userID, postID);
        posts = profile.getPosts();
        Assert.assertNotNull(posts);
        Assert.assertEquals(0, posts.size());

        // test model methods
        try {
            String serializedModel = null;
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            profile.getModel().write(writer);
            serializedModel = new String(writer.toByteArray(), StandardCharsets.UTF_8);
            writer.close();

            ByteArrayInputStream reader = new ByteArrayInputStream(serializedModel.getBytes());
            profile.addModel(reader);
            reader.close();

            Assert.assertEquals(name, profile.getName(userID));
            friends = profile.getFriends();
            Assert.assertNotNull(friends);
            Assert.assertEquals(0, friends.size());
            posts = profile.getPosts();
            Assert.assertNotNull(posts);
            Assert.assertEquals(0, posts.size());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

}
