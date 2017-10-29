package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.Network;

public class NetworkTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        Network testNetwork = new Network();

        Assert.assertEquals("http://", Network.NETWORK_PROTOCOL);
        Assert.assertEquals("localhost", Network.LOCALHOST_ADDRESS);
        Assert.assertEquals(25000, Network.CLIENT_WEBSERVER_PORT);
        Assert.assertEquals(25700, Network.SERVER_WEBSERVICE_PORT);
        Assert.assertEquals("hub_service", Network.NETWORK_HUB);
        Assert.assertEquals(25800, Network.NETWORK_HUB_PORT);
        Assert.assertEquals(Network.NETWORK_PROTOCOL + Network.LOCALHOST_ADDRESS + ":" + Network.CLIENT_WEBSERVER_PORT,
                Network.LOCALHOST_URL);

        Assert.assertEquals(Network.NETWORK_PROTOCOL, testNetwork.NETWORK_PROTOCOL);
        Assert.assertEquals(Network.LOCALHOST_ADDRESS, testNetwork.LOCALHOST_ADDRESS);
        Assert.assertEquals(Network.CLIENT_WEBSERVER_PORT, testNetwork.CLIENT_WEBSERVER_PORT);
        Assert.assertEquals(Network.SERVER_WEBSERVICE_PORT, testNetwork.SERVER_WEBSERVICE_PORT);
        Assert.assertEquals(Network.NETWORK_HUB, testNetwork.NETWORK_HUB);
        Assert.assertEquals(Network.NETWORK_HUB_PORT, testNetwork.NETWORK_HUB_PORT);
        Assert.assertEquals(Network.LOCALHOST_URL, testNetwork.LOCALHOST_URL);
    }

}
