package test.java.message;

import org.junit.Assert;
import org.junit.Test;

import main.java.message.RegisterMessage;
import main.java.message.UpdateMessage;

public class UpdateMessageTest {

    @Test
    public void test() {
        String resource = "http://localhost/user/profile";
        String userID = "user@localhost";
        UpdateMessage message = new UpdateMessage();

        // test resource methods
        message.setResource("test");
        Assert.assertEquals("test", message.getResource());
        message.setResource(resource);
        Assert.assertEquals(resource, message.getResource());

        // test userID methods
        message.setUserID("test");
        Assert.assertEquals("test", message.getUserID());
        message.setUserID(userID);
        Assert.assertEquals(userID, message.getUserID());

        // test convert function
        String testString = null;
        Assert.assertNull(UpdateMessage.parse(testString));
        testString = "Test123";
        Assert.assertNull(UpdateMessage.parse(testString));
        RegisterMessage registerMessage = new RegisterMessage();
        Assert.assertNull(UpdateMessage.parse(registerMessage.toString()));
        UpdateMessage testMessage = UpdateMessage.parse(message.getMessage());
        Assert.assertNotNull(testMessage);
        Assert.assertEquals(message.getMessage(), testMessage.getMessage());
    }

}
