package test.java.message;

import org.junit.Assert;
import org.junit.Test;

import main.java.message.LoginMessage;
import main.java.message.RegisterMessage;

public class RegisterMessageTest {

    @Test
    public void test() {
        RegisterMessage message = new RegisterMessage();

        // test type
        Assert.assertEquals("register", message.getType());

        // test mail
        String mail = "my_nickname";
        Assert.assertTrue(message.setNickname(mail));
        Assert.assertFalse(message.setNickname(mail));
        Assert.assertEquals(mail, message.getNickname());

        // test password
        Assert.assertFalse(message.setPassword("1"));
        Assert.assertFalse(message.setPassword("a"));
        Assert.assertFalse(message.setPassword("."));
        Assert.assertFalse(message.setPassword("D"));
        Assert.assertFalse(message.setPassword("g3"));
        Assert.assertFalse(message.setPassword("Hg7"));
        Assert.assertFalse(message.setPassword("h_U78"));
        Assert.assertFalse(message.setPassword("?J3h"));
        Assert.assertFalse(message.setPassword("hj1U_5Z"));
        Assert.assertFalse(message.setPassword("1 9?"));

        String password = "G8_j?H4%i9.6p";
        Assert.assertFalse(message.setPassword("G8_j?H4%i"));
        Assert.assertTrue(message.setPassword(password));
        Assert.assertFalse(message.setPassword(password));
        Assert.assertEquals(password, message.getPassword());

        // test convert function
        String testString = null;
        Assert.assertNull(RegisterMessage.parse(testString));
        testString = "Test123";
        Assert.assertNull(RegisterMessage.parse(testString));
        LoginMessage loginMessage = new LoginMessage();
        Assert.assertNull(RegisterMessage.parse(loginMessage.getMessage()));
        RegisterMessage testMessage = RegisterMessage.parse(message.getMessage());
        Assert.assertNotNull(testMessage);
        Assert.assertEquals("register", testMessage.getType());
        Assert.assertEquals(mail, testMessage.getNickname());
        Assert.assertEquals(password, testMessage.getPassword());
        Assert.assertEquals(message.getMessage(), testMessage.getMessage());
    }
}
