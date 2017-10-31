package test.java.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.junit.Assert;
import org.junit.Test;

import main.java.connection.TCPConnection;

public class TCPConnectionTest {

    @Test
    public void test() {
        try {
            int port = 20000;
            TestServer server = new TestServer(port);
            server.start();
            TCPConnection connection = new TCPConnection(InetAddress.getByName("localhost"), port);

            Assert.assertFalse(connection.hasData());
            Thread.sleep(2000);
            Assert.assertTrue(connection.hasData());
            Assert.assertEquals("test", connection.getData());
            Assert.assertEquals("127.0.0.1", connection.getInetAddress());
            Assert.assertTrue(connection.getRemotePort() >= 0 && connection.getRemotePort() <= 65535);

            server.join();
            Assert.assertFalse(connection.isConnected());
            Assert.assertNull(TestServer.error);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }
}

class TestServer extends Thread {

    public static AssertionError error = null;
    private ServerSocket socket;

    public TestServer(int port) throws IOException {
        socket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            TCPConnection connection = new TCPConnection(socket.accept());

            Thread.sleep(1000);
            connection.sendData("test");
            Assert.assertEquals("127.0.0.1", connection.getLocalAddress());
            Assert.assertTrue(connection.getRemotePort() >= 0 && connection.getLocalPort() <= 65535);

            connection.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            error = new AssertionError();
        } catch (AssertionError ae) {
            ae.printStackTrace();
            error = ae;
        }
    }
}
