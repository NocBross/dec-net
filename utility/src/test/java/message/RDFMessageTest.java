package test.java.message;

import org.junit.Assert;
import org.junit.Test;

import main.java.message.RDFMessage;
import main.java.message.RegisterMessage;

public class RDFMessageTest {

    @Test
    public void test() {
        String resourceID = "some_ID";
        String model = "some_model";
        RDFMessage message = new RDFMessage(resourceID, model);

        // test initial values and getter
        Assert.assertEquals("rdf", message.getType());
        Assert.assertEquals(resourceID, message.getResourceID());
        Assert.assertEquals(model, message.getModel());

        // test convert
        String testString = null;
        Assert.assertNull(RDFMessage.parse(testString));
        testString = "Test123";
        Assert.assertNull(RDFMessage.parse(testString));
        RegisterMessage registerMessage = new RegisterMessage();
        Assert.assertNull(RDFMessage.parse(registerMessage.toString()));
        RDFMessage testMessage = RDFMessage.parse(message.getMessage());
        Assert.assertNotNull(testMessage);
        Assert.assertEquals(message.getMessage(), testMessage.getMessage());
    }

}
