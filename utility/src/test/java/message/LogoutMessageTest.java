package test.java.message;

import org.junit.Assert;
import org.junit.Test;

import main.java.message.LoginMessage;
import main.java.message.LogoutMessage;

public class LogoutMessageTest {

    @Test
    public void test() {
        LogoutMessage message = new LogoutMessage();
        String testString = null;

        // test initialize values
        Assert.assertEquals("logout", message.getType());

        // test sender methods
        Assert.assertTrue(message.setSender("my_nickname"));
        Assert.assertFalse(message.setSender("test@mail.de"));
        Assert.assertFalse(message.setSender("test"));
        Assert.assertEquals("my_nickname", message.getSender());

        // test convert method
        Assert.assertNull(LogoutMessage.parse(testString));
        testString = "test";
        Assert.assertNull(LogoutMessage.parse(testString));
        LoginMessage loginMessage = new LoginMessage();
        Assert.assertNull(LogoutMessage.parse(loginMessage.getMessage()));
        Assert.assertEquals(message.getMessage(), LogoutMessage.parse(message.getMessage()).getMessage());
    }
}
