package test.java.message;

import org.junit.Assert;
import org.junit.Test;

import main.java.message.LoginMessage;
import main.java.message.RegisterMessage;

public class LoginMessageTest {

    @Test
    public void test() {
        LoginMessage message = new LoginMessage();

        // test type
        Assert.assertEquals("login", message.getType());

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
        Assert.assertTrue(message.setPassword(password));
        Assert.assertFalse(message.setPassword("G8_j?H4%i9"));
        Assert.assertEquals(password, message.getPassword());

        // test convert function
        String testString = null;
        Assert.assertNull(LoginMessage.parse(testString));
        testString = "Test123";
        Assert.assertNull(LoginMessage.parse(testString));
        RegisterMessage registerMessage = new RegisterMessage();
        Assert.assertNull(LoginMessage.parse(registerMessage.toString()));
        LoginMessage testMessage = LoginMessage.parse(message.getMessage());
        Assert.assertNotNull(testMessage);
        Assert.assertEquals(message.getMessage(), testMessage.getMessage());
    }
}
