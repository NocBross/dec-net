package test.java.message;

import org.junit.Assert;
import org.junit.Test;

import main.java.message.RegisterMessage;
import main.java.message.UserIDMessage;

public class UserIDMessageTest {

    @Test
    public void test() {
        UserIDMessage message = new UserIDMessage();

        // test type
        Assert.assertEquals("userIDMessage", message.getType());

        // test mail
        String userID = "test@mail.de";
        Assert.assertFalse(message.setUserID("test @"));
        Assert.assertTrue(message.setUserID(userID));
        Assert.assertEquals(userID, message.getUserID());

        // test convert function
        String testString = null;
        Assert.assertNull(UserIDMessage.parse(testString));
        testString = "Test123";
        Assert.assertNull(UserIDMessage.parse(testString));
        RegisterMessage registerMessage = new RegisterMessage();
        Assert.assertNull(UserIDMessage.parse(registerMessage.toString()));
        UserIDMessage testMessage = UserIDMessage.parse(message.getMessage());
        Assert.assertNotNull(testMessage);
        Assert.assertEquals(message.getMessage(), testMessage.getMessage());
    }

}
