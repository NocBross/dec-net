package test.java.abstraction;

import org.junit.Assert;
import org.junit.Test;

import main.java.client_agent.abstraction.TransmitData;

public class TransmitDataTest {

    @Test
    public void test() {
        String destination = "test@mail.de";
        String message = "test";
        TransmitData tcpPackage = new TransmitData(destination, message);

        // test initial values
        Assert.assertEquals(destination, tcpPackage.getDestination());
        Assert.assertEquals(message, tcpPackage.getMessage());

        // test tcp data
        Assert.assertEquals(destination, tcpPackage.getDestination());
        Assert.assertEquals(message, tcpPackage.getMessage());
    }
}
