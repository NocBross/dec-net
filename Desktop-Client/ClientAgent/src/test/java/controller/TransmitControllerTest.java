package test.java.controller;

import java.net.ServerSocket;

import org.junit.Assert;
import org.junit.Test;

import main.java.connection.TCPConnection;
import main.java.message.LoginMessage;

public class TransmitControllerTest {

    @Test
    public void test() {
        // try {
        // ConnectionModel connectionModel = new ConnectionModel();
        // LoginMessage message = new LoginMessage();
        // TransmitModel transmitModel = new TransmitModel();
        // TransmitController transmitController = new
        // TransmitController(connectionModel, transmitModel);
        // TransmitControllerTestServer server = new
        // TransmitControllerTestServer(Network.NETWORK_HUB_PORT, message);
        //
        // // initialize test
        // connectionModel.setServerAddress(InetAddress.getByName("localhost"));
        // message.setNickname("test@mail.de");
        // message.setPassword("123ABC!def?");
        // server.start();
        // transmitController.start();
        // Thread.sleep(50);
        //
        // // test login port
        // Assert.assertTrue(server.isAlive());
        // Assert.assertTrue(transmitController.isAlive());
        // transmitController.addMessage(Network.NETWORK_HUB, message.getMessage());
        // Thread.sleep(500);
        // Assert.assertTrue(transmitModel.isMessageQueueEmpty());
        //
        // // clean up
        // server.join();
        // transmitController.stopController();
        // transmitController.join();
        // Assert.assertFalse(server.isAlive());
        // Assert.assertFalse(transmitController.isAlive());
        // Assert.assertNull(server.error);
        // } catch (Exception e) {
        // e.printStackTrace();
        // throw new AssertionError();
        // }
    }
}

class TransmitControllerTestServer extends Thread {

    public AssertionError error;

    private int port;
    private LoginMessage message;

    public TransmitControllerTestServer(int port, LoginMessage message) {
        error = null;
        this.port = port;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            TCPConnection connection = new TCPConnection(serverSocket.accept());

            String incomingMessage = connection.getData();
            Assert.assertEquals(message.getMessage(), incomingMessage);
            do {
                incomingMessage = connection.getData();
            } while (incomingMessage.equals("1"));
            Assert.assertEquals(message.getMessage(), incomingMessage);

            connection.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            error = new AssertionError();
        } catch (AssertionError ae) {
            ae.printStackTrace();
            error = ae;
        }
    }

}
