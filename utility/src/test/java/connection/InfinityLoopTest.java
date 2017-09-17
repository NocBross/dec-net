package test.java.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import org.junit.Assert;
import org.junit.Test;

import main.java.connection.TCPConnection;

public class InfinityLoopTest {

    @Test
    public void test() {
        try {
            int port = 20000;
            TestInfinityServer server = new TestInfinityServer(port);
            server.start();
            TCPConnection connection = new TCPConnection(InetAddress.getByName("localhost"), port);

            Thread.sleep(65000);
            connection = new TCPConnection(InetAddress.getByName("localhost"), port);
            Assert.assertTrue(connection.isConnected());

            server.join();
            Assert.assertFalse(connection.isConnected());
            Assert.assertNull(TestServer.error);
            connection.close();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            throw new AssertionError();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

}

class TestInfinityServer extends Thread {

    public static AssertionError error = null;
    private ServerSocket socket;
    // end of attributes

    public TestInfinityServer(int port) throws IOException {
        socket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 2; i++) {
                TCPConnection connection = new TCPConnection(socket.accept());

                try {
                    String input = connection.getData();
                    if (i == 0) {
                        error = new AssertionError();
                        System.out.println("Error: no timeout triggered");
                    } else {
                        Thread.sleep(1000);
                        Assert.assertEquals("1", input);
                    }
                } catch (SocketTimeoutException ste) {
                    Assert.assertEquals("Read timed out", ste.getMessage());
                } catch (InterruptedException ie) {
                    error = new AssertionError();
                    ie.printStackTrace();
                }

                connection.close();
            }
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
