package test.java.message;

import org.junit.Assert;
import org.junit.Test;

import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;

public class ReportMessageTest {

    @Test
    public void test() {
        ReportMessage message = new ReportMessage();

        // test initial values
        Assert.assertEquals("report", message.getType());
        Assert.assertNull(message.getReferencedMessage());
        Assert.assertFalse(message.getResult());
        Assert.assertEquals(-1, message.getStatusCode());

        // test referenced message
        String refMessage = "login";
        Assert.assertTrue(message.setReferencedMessage(refMessage));
        Assert.assertFalse(message.setReferencedMessage(refMessage));
        Assert.assertFalse(message.setReferencedMessage("register"));
        Assert.assertEquals(refMessage, message.getReferencedMessage());

        // test result
        boolean result = true;
        Assert.assertTrue(message.setResult(result));
        Assert.assertFalse(message.setResult(result));
        Assert.assertFalse(message.setResult(false));
        Assert.assertTrue(message.getResult());

        // test error code
        int statusCode = 1896514;
        Assert.assertTrue(message.setStatusCode(statusCode));
        Assert.assertFalse(message.setStatusCode(statusCode));
        Assert.assertFalse(message.setStatusCode(-8));
        Assert.assertEquals(statusCode, message.getStatusCode());

        // test convert
        String testString = null;
        Assert.assertNull(ReportMessage.parse(testString));
        testString = "Test123";
        Assert.assertNull(ReportMessage.parse(testString));
        RegisterMessage registerMessage = new RegisterMessage();
        Assert.assertNull(ReportMessage.parse(registerMessage.toString()));
        ReportMessage testMessage = ReportMessage.parse(message.getMessage());
        Assert.assertNotNull(testMessage);
        Assert.assertEquals(message.getMessage(), testMessage.getMessage());
    }
}
