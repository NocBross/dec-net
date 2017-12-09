package test.java.message;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import main.java.message.AddressMessage;
import main.java.message.RegisterMessage;

public class AddressMessageTest {

    @Test
    public void test() {
        int externalPort = 16359;
        int localPort = 26500;
        String localAddress = "192.168.2.100";
        String externalAddress = "2001:db8:0:8d3:0:8a2e:70:7344";
        String userID = "test@mail";
        AddressMessage message = new AddressMessage();
        JSONObject wrongMessage = new JSONObject();

        // test type
        Assert.assertEquals("ip_address", message.getType());

        // test local port
        Assert.assertEquals(-1, message.getLocalPort());
        Assert.assertFalse(message.setLocalPort(-25));
        Assert.assertFalse(message.setLocalPort(2596328));
        Assert.assertTrue(message.setLocalPort(localPort));
        Assert.assertEquals(localPort, message.getLocalPort());

        // test external port
        Assert.assertEquals(-1, message.getExternalPort());
        Assert.assertFalse(message.setExternalPort(-25));
        Assert.assertFalse(message.setExternalPort(2596328));
        Assert.assertTrue(message.setExternalPort(externalPort));
        Assert.assertEquals(externalPort, message.getExternalPort());

        // test local address
        Assert.assertNull(message.getLocalAddress());
        Assert.assertFalse(message.setLocalAddress("test"));
        Assert.assertFalse(message.setLocalAddress("192.169"));
        Assert.assertTrue(message.setLocalAddress(localAddress));
        Assert.assertEquals(localAddress, message.getLocalAddress());

        // test external address
        Assert.assertNull(message.getExternalAddress());
        Assert.assertFalse(message.setExternalAddress("test"));
        Assert.assertFalse(message.setExternalAddress("192.169"));
        Assert.assertTrue(message.setExternalAddress(externalAddress));
        Assert.assertEquals(externalAddress, message.getExternalAddress());

        // test userID
        Assert.assertNull(message.getUserID());
        Assert.assertFalse(message.setUserID("test"));
        Assert.assertTrue(message.setUserID(userID));
        Assert.assertEquals(userID, message.getUserID());

        // test convert function
        String testString = null;
        Assert.assertNull(AddressMessage.parse(testString));
        testString = "Test123";
        Assert.assertNull(AddressMessage.parse(testString));
        RegisterMessage registerMessage = new RegisterMessage();
        Assert.assertNull(AddressMessage.parse(registerMessage.toString()));
        AddressMessage testMessage = AddressMessage.parse(message.getMessage());
        Assert.assertNotNull(testMessage);
        Assert.assertEquals(message.getMessage(), testMessage.getMessage());

        wrongMessage.put("type", AddressMessage.ID);
        wrongMessage.put("method", "test");
        AddressMessage dummy = AddressMessage.parse(wrongMessage.toString());
        Assert.assertEquals(-1, dummy.getLocalPort());
        Assert.assertEquals(-1, dummy.getExternalPort());
        Assert.assertNull(dummy.getLocalAddress());
        Assert.assertNull(dummy.getExternalAddress());
        Assert.assertNull(dummy.getUserID());
    }

}
