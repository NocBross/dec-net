package test.java.message;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.UserStatus;
import main.java.message.RegisterMessage;
import main.java.message.StatusMessage;

public class StatusMessageTest {

    @Test
    public void test() {
        String testNickname1 = "myID1";
        String testNickname2 = "myID2";
        String testNickname3 = "myID3";
        UserStatus testStatus1 = UserStatus.OFFLINE;
        UserStatus testStatus2 = UserStatus.ONLINE;
        UserStatus testStatus3 = UserStatus.BUSY;
        StatusMessage message = new StatusMessage();

        // test initial values
        Assert.assertEquals("status", message.getType());
        Assert.assertNotNull(message.getUsers());
        Assert.assertFalse(message.getUsers().hasNext());

        // test user methods
        Assert.assertNull(message.getStatus(testNickname1));
        Assert.assertFalse(message.addUser(testNickname1, null));
        Assert.assertTrue(message.addUser(testNickname1, testStatus1));
        Assert.assertFalse(message.addUser(testNickname1, testStatus1));
        Assert.assertFalse(message.addUser(testNickname1, null));
        Assert.assertTrue(message.addUser(testNickname2, testStatus2));
        Assert.assertTrue(message.addUser(testNickname3, testStatus3));
        Assert.assertEquals(testStatus1, message.getStatus(testNickname1));
        Assert.assertEquals(testStatus2, message.getStatus(testNickname2));
        Assert.assertEquals(testStatus3, message.getStatus(testNickname3));

        // test convert
        String testString = null;
        Assert.assertNull(StatusMessage.parse(testString));
        testString = "Test123";
        Assert.assertNull(StatusMessage.parse(testString));
        RegisterMessage registerMessage = new RegisterMessage();
        Assert.assertNull(StatusMessage.parse(registerMessage.toString()));
        StatusMessage testMessage = StatusMessage.parse(message.getMessage());
        Assert.assertNotNull(testMessage);
        Assert.assertEquals(message.getMessage(), testMessage.getMessage());
    }

}
