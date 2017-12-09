package test.java.connection;

import org.junit.Assert;
import org.junit.Test;

import main.java.connection.IPAddressData;

public class IPAddressDataTest {

    @Test
    public void test() {
        int activePort = 26000;
        int externalPort = 45978;
        int localPort = activePort;
        String activeAddress = "192.168.2.100";
        String externalAddress = "45.156.211.1";
        String localeAddress = activeAddress;
        IPAddressData data = new IPAddressData();

        // test initial values
        Assert.assertEquals(-1, data.getActivePort());
        Assert.assertEquals(-1, data.getExternalPort());
        Assert.assertEquals(-1, data.getLocalPort());
        Assert.assertNull(data.getActiveAddress());
        Assert.assertNull(data.getExternalAddress());
        Assert.assertNull(data.getLocalAddress());

        // test active port methods
        data.setActivePort(1);
        Assert.assertEquals(1, data.getActivePort());
        data.setActivePort(activePort);
        Assert.assertEquals(activePort, data.getActivePort());

        // test external port
        data.setExternalPort(1);
        Assert.assertEquals(1, data.getExternalPort());
        data.setExternalPort(externalPort);
        Assert.assertEquals(externalPort, data.getExternalPort());

        // test external port
        data.setLocalPort(1);
        Assert.assertEquals(1, data.getLocalPort());
        data.setLocalPort(localPort);
        Assert.assertEquals(localPort, data.getLocalPort());

        // test active address methods
        data.setActiveAddress("test");
        Assert.assertEquals("test", data.getActiveAddress());
        data.setActiveAddress(activeAddress);
        Assert.assertEquals(activeAddress, data.getActiveAddress());

        // test external address methods
        data.setExternalAddress("test");
        Assert.assertEquals("test", data.getExternalAddress());
        data.setExternalAddress(externalAddress);
        Assert.assertEquals(externalAddress, data.getExternalAddress());

        // test local address methods
        data.setLocalAddress("test");
        Assert.assertEquals("test", data.getLocalAddress());
        data.setLocalAddress(localeAddress);
        Assert.assertEquals(localeAddress, data.getLocalAddress());
    }

}
