package test.java.controller;

import java.net.InetAddress;
import java.net.ServerSocket;

import org.junit.Assert;
import org.junit.Test;

import main.java.client_agent.abstraction.ConnectionModel;
import main.java.client_agent.abstraction.TransmitModel;
import main.java.client_agent.controller.TransmitController;
import main.java.connection.TCPConnection;
import main.java.constants.EndPoint;
import main.java.constants.Port;

public class TransmitControllerTest {

    @Test
    public void test() {
        try {
            String message = "login message";
            ConnectionModel connectionModel = new ConnectionModel(InetAddress.getByName("localhost"));
            TransmitModel transmitModel = new TransmitModel();
            TransmitController transmitController = new TransmitController(connectionModel, transmitModel);
            TransmitControllerTestServer server = new TransmitControllerTestServer(Port.LOGIN_SERVICE, message);

            // initialize test
            server.start();
            transmitController.start();
            Thread.sleep(50);

            // test login port
            Assert.assertTrue(server.isAlive());
            Assert.assertTrue(transmitController.isAlive());
            transmitController.addMessage(EndPoint.LOGIN_END_POINT, message);
            Thread.sleep(500);
            Assert.assertTrue(transmitModel.isMessageQueueEmpty());
            transmitController.addMessage(EndPoint.LOGIN_END_POINT, message);
            Thread.sleep(500);
            Assert.assertTrue(transmitModel.isMessageQueueEmpty());

            // clean up
            server.join();
            transmitController.stopController();
            transmitController.join();
            Assert.assertFalse(server.isAlive());
            Assert.assertFalse(transmitController.isAlive());
            Assert.assertNull(server.error);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }
}

class TransmitControllerTestServer extends Thread {

    public AssertionError error;

    private int port;
    private String message;

    public TransmitControllerTestServer(int port, String message) {
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
            Assert.assertEquals(message, incomingMessage);
            do {
                incomingMessage = connection.getData();
            } while (incomingMessage.equals("1"));
            Assert.assertEquals(message, incomingMessage);

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
