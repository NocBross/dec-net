package test.java.abstraction;

import java.net.InetAddress;
import java.net.ServerSocket;

import org.junit.Assert;
import org.junit.Test;

import main.java.client_agent.abstraction.ConnectionModel;
import main.java.connection.TCPConnection;
import main.java.constants.Network;
import test.java.TestData;

public class ConnectionModelTest {

    @Test
    public void test() {
        try {
            ConnectionModel model = new ConnectionModel();
            ConnectionModelTestSever server = new ConnectionModelTestSever();
            server.start();
            Thread.sleep(50);

            // test server connection methods
            model.setServerAddress(InetAddress.getByName(TestData.SERVER_ADDRESS));
            Assert.assertTrue(model.addHubConnection(Network.NETWORK_HUB));
            Assert.assertNotNull(model.getServerConnection());
            Assert.assertEquals(Network.NETWORK_HUB_PORT, model.getServerConnection().getRemotePort());
            Assert.assertFalse(model.addHubConnection(Network.NETWORK_HUB));
            Assert.assertTrue(model.deleteServerConnection());
            Assert.assertNull(model.getServerConnection());
            Assert.assertFalse(model.addHubConnection(Network.NETWORK_HUB));
            Assert.assertNull(model.getServerConnection());
            Assert.assertFalse(model.deleteServerConnection());

            // test errors
            server.join();
            Assert.assertNull(server.error);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }
}

class ConnectionModelTestSever extends Thread {

    public AssertionError error;

    public ConnectionModelTestSever() {
        error = null;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(Network.NETWORK_HUB_PORT);
            TCPConnection connection = new TCPConnection(socket.accept());

            connection.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            error = new AssertionError();
        }
    }
}
