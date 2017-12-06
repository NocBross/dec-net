package test.java.rdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import main.java.rdf.PostRDF;

public class PostRDFTest {

    @Test
    public void test() {
        String creator = "test_user@localhost";
        String content = "some test";
        String reply1 = "http://localhost/test_user/post/37598";
        String reply2 = "http://localhost/test_user/post/37594";
        PostRDF post = new PostRDF();
        List<String> replys = null;

        // test content methods
        post.setContent(content);
        Assert.assertEquals(content, post.getContent());

        // test creator methods
        post.setCreator(creator);
        Assert.assertEquals(creator, post.getCreator());

        // test reply methods
        post.addReply(reply1);
        post.addReply(reply2);
        replys = post.getReplys();
        Assert.assertNotNull(replys);
        Assert.assertEquals(2, replys.size());
        Assert.assertEquals(reply2, replys.get(0));
        Assert.assertEquals(reply1, replys.get(1));

        try {
            String serializedModel = null;
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            post.getModel().write(writer);
            serializedModel = new String(writer.toByteArray(), StandardCharsets.UTF_8);
            writer.close();

            ByteArrayInputStream reader = new ByteArrayInputStream(serializedModel.getBytes());
            post.setModel(reader);
            reader.close();

            Assert.assertEquals(content, post.getContent());
            Assert.assertEquals(creator, post.getCreator());
            replys = post.getReplys();
            Assert.assertNotNull(replys);
            Assert.assertEquals(2, replys.size());
            Assert.assertEquals(reply2, replys.get(0));
            Assert.assertEquals(reply1, replys.get(1));
        } catch(Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

}
