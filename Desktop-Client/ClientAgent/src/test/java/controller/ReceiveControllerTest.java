package test.java.controller;

import java.net.ServerSocket;

import org.junit.Test;

import main.java.connection.TCPConnection;

public class ReceiveControllerTest {

    @Test
    public void test() {
        try {
            // String message = "test message";
            // ClientAgent agent = Mockito.mock(ClientAgent.class);
            // ConnectionModel connectionModel = new ConnectionModel();
            // ReceiveController receiveController = new ReceiveController(connectionModel,
            // agent);
            // ReceiveControllerTestServer testServer = new
            // ReceiveControllerTestServer(Network.NETWORK_HUB_PORT, message);
            //
            // // initialize test
            // connectionModel.setServerAddress(InetAddress.getByName("localhost"));
            // testServer.start();
            // List<String> spyMessageList = Mockito.spy(new LinkedList<String>());
            // Mockito.doAnswer(new Answer<String>() {
            //
            // @Override
            // public String answer(InvocationOnMock invocation) throws Throwable {
            // spyMessageList.add((String) invocation.getArguments()[0]);
            // return null;
            // }
            //
            // }).when(agent).updateMessage(Mockito.anyString());
            // receiveController.start();
            // connectionModel.addHubConnection(Network.NETWORK_HUB);
            //
            // // test
            // Thread.sleep(750);
            // Assert.assertEquals(2, spyMessageList.size());
            // Assert.assertEquals(message, spyMessageList.get(0));
            // Assert.assertEquals(message, spyMessageList.get(1));
            //
            // // clean up
            // testServer.join();
            // receiveController.stopController();
            // receiveController.join();
            // Assert.assertFalse(receiveController.isAlive());
            // Assert.assertFalse(testServer.isAlive());
            // Assert.assertNull(testServer.error);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }
}

class ReceiveControllerTestServer extends Thread {

    public AssertionError error;

    private int port;
    private String message;

    public ReceiveControllerTestServer(int port, String message) {
        error = null;
        this.port = port;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);
            TCPConnection connection = new TCPConnection(socket.accept());

            Thread.sleep(500);
            if (connection.isConnected()) {
                connection.sendData(message);
                connection.sendData(message);
            }
            Thread.sleep(31000);

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
