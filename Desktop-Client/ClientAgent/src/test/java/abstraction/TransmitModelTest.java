package test.java.abstraction;

import org.junit.Assert;
import org.junit.Test;

import main.java.client_agent.abstraction.TransmitData;
import main.java.client_agent.abstraction.TransmitModel;
import test.java.TestData;

public class TransmitModelTest {

    @Test
    public void test() {
        String message = "new message";
        TransmitData data = new TransmitData(TestData.NICKNAME_2, message);
        TransmitData storedData = null;
        TransmitModel model = new TransmitModel();

        // test initial state
        Assert.assertTrue(model.isMessageQueueEmpty());

        // test message methods
        Assert.assertTrue(model.isMessageQueueEmpty());
        model.addMessage(data);
        Assert.assertFalse(model.isMessageQueueEmpty());
        storedData = model.getMessage();
        Assert.assertEquals(TestData.NICKNAME_2, storedData.getDestination());
        Assert.assertEquals(message, storedData.getMessage());
    }
}
