package test.java.message;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import main.java.message.CacheMessage;
import main.java.message.RegisterMessage;

public class CacheMessageTest {

    @Test
    public void test() {
        String data = "some test data";
        String requestMethod = "POST";
        String resource = "http://localhost/test_user";
        CacheMessage message = new CacheMessage();
        JSONObject wrongMessage = new JSONObject();

        // test data methods
        message.setData("test");
        Assert.assertEquals("test", message.getData());
        message.setData(data);
        Assert.assertEquals(data, message.getData());

        // test requestMethod methods
        Assert.assertFalse(message.setRequestMethod("test"));
        Assert.assertTrue(message.setRequestMethod("GET"));
        Assert.assertEquals("GET", message.getRequestMethod());
        Assert.assertTrue(message.setRequestMethod(requestMethod));
        Assert.assertEquals(requestMethod, message.getRequestMethod());

        // test resource method
        message.setResource("test_res");
        Assert.assertEquals("test_res", message.getResource());
        message.setResource(resource);
        Assert.assertEquals(resource, message.getResource());

        // test convert function
        String testString = null;
        Assert.assertNull(CacheMessage.parse(testString));
        testString = "Test123";
        Assert.assertNull(CacheMessage.parse(testString));
        RegisterMessage registerMessage = new RegisterMessage();
        Assert.assertNull(CacheMessage.parse(registerMessage.toString()));
        CacheMessage testMessage = CacheMessage.parse(message.getMessage());
        Assert.assertNotNull(testMessage);
        Assert.assertEquals(message.getMessage(), testMessage.getMessage());

        wrongMessage.put("type", CacheMessage.ID);
        wrongMessage.put("method", "test");
        Assert.assertNull(CacheMessage.parse(wrongMessage.toString()));
    }

}
