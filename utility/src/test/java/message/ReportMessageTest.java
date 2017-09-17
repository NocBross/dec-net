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
        Assert.assertNull(message.getErrorCode());

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
        String errorCode = "test error code";
        Assert.assertTrue(message.setErrorCode(errorCode));
        Assert.assertFalse(message.setErrorCode(errorCode));
        Assert.assertFalse(message.setErrorCode("another code"));
        Assert.assertEquals(errorCode, message.getErrorCode());

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
