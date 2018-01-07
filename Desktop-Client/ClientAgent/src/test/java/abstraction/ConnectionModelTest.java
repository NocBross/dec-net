package test.java.abstraction;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.junit.Assert;
import org.junit.Test;

import main.java.client_agent.abstraction.ConnectionModel;
import main.java.connection.TCPConnection;
import main.java.constants.Network;
import main.java.service.CustomLogger;
import test.java.TestData;

public class ConnectionModelTest {

    @Test
    public void test() {
        try {
            String path = "logs/log.txt";
            CustomLogger logger = new CustomLogger(path);
            ConnectionModel model = new ConnectionModel(logger);
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
            logger.close();
            File log = new File(path);
            log.delete();
            log.getParentFile().delete();
        } catch(Exception e) {
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
        } catch(Exception e) {
            e.printStackTrace();
            error = new AssertionError();
        }
    }
}
