package test.java.services;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import main.java.connection.TCPConnection;
import main.java.constants.LogFiles;
import main.java.message.CacheMessage;
import main.java.message.LogoutMessage;
import main.java.message.UpdateMessage;
import main.java.services.ShippingService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class ShippingServiceTest {

    private class TestServer extends Thread {

        public AssertionError error;

        private int connectionID;
        private int numberOfConnections;
        private String data;
        private String resource;
        private String userID;
        private String[] connectionIDs;
        private DatabaseConnector database;
        private ServerSocket socket;
        private ShippingService service;
        private TCPConnection[] connections;

        public TestServer(int numberOfConnections, int port, String data, String resource, String[] connectionIDs,
                int connectionID, String userID) throws Exception {
            this.connectionID = connectionID;
            this.numberOfConnections = numberOfConnections;
            this.data = data;
            this.resource = resource;
            this.userID = userID;
            error = null;
            socket = new ServerSocket(port);
            ServerSecrets secrets = new ServerSecrets();
            secrets.loadServerSecrets();
            database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
            service = new ShippingService(secrets);
            connections = new TCPConnection[numberOfConnections];
            this.connectionIDs = connectionIDs;
        }

        public void run() {
            try {
                // initialize
                service.start();
                for (int i = 0; i < numberOfConnections; i++) {
                    Assert.assertTrue(database.insertNewUser(connectionIDs[i], "123test"));
                    connections[i] = new TCPConnection(socket.accept());
                    service.addConnection(connectionIDs[i], connections[i]);
                }

                // test
                testResource();
                testUpdate();
                Thread.sleep(5000);

                // clean up
                for (int i = 0; i < numberOfConnections; i++) {
                    connections[i].close();
                    database.deleteUser(connectionIDs[i]);
                }
                database.deleteUser(userID);
                Assert.assertEquals(0, database.readCache(resource).size());
                database.closeConnection();
                socket.close();
                File log = new File(LogFiles.SHIPPING_LOG);
                for (File file : log.getParentFile().listFiles()) {
                    Assert.assertTrue(file.delete());
                }
                Assert.assertTrue(log.getParentFile().delete());
                service.stopService();
                service.join();
            } catch (Exception e) {
                e.printStackTrace();
                error = new AssertionError();
            } catch (AssertionError ae) {
                ae.printStackTrace();
                error = ae;
            }
        }

        private void testResource() throws Exception {
            try {
                List<String> cache = null;

                service.addShippingPackage(data, resource);
                Thread.sleep(10000);
                cache = database.readCache(resource);
                Assert.assertEquals(3, cache.size());
                Collections.sort(cache);
                for (int i = 0; i < cache.size(); i++) {
                    Assert.assertEquals(connectionIDs[i], cache.get(i));
                }
                String receivedData = service.getResource(resource);
                Assert.assertEquals(data, receivedData);
            } catch (AssertionError ae) {
                ae.printStackTrace();
                error = ae;
            }
        }

        private void testUpdate() throws Exception {
            try {
                UpdateMessage message = new UpdateMessage();

                message.setResource(resource);
                message.setUserID(userID);

                Assert.assertTrue(database.insertNewUser(userID, "123test"));
                Assert.assertTrue(service.sendUpdate(connectionIDs[connectionID], message));
                Assert.assertFalse(service.sendUpdate(userID, message));
            } catch (AssertionError ae) {
                ae.printStackTrace();
                error = ae;
            }
        }
    }

    @Test
    public void test() {
        try {
            boolean hasToSendData = false;
            int connectionID = 0;
            int connectionHasToSendData = -1;
            int numberOfConnections = 3;
            int port = 15692;
            String data = "test data";
            String resource = "/test/post/1";
            String userID = "user@localhost";
            String[] connectionIDs = new String[numberOfConnections];
            UpdateMessage updateMessage = null;
            CacheMessage message = null;
            CacheMessage[] messages = new CacheMessage[numberOfConnections];
            TestServer server = new TestServer(numberOfConnections, port, data, resource, connectionIDs, connectionID,
                    userID);
            TCPConnection[] connections = new TCPConnection[numberOfConnections];
            for (int i = 0; i < numberOfConnections; i++) {
                connectionIDs[i] = "user_" + (i + 1) + "@localhost";
            }

            server.start();
            for (int i = 0; i < numberOfConnections; i++) {
                messages[i] = null;
                connections[i] = new TCPConnection(InetAddress.getByName("localhost"), port);
            }

            // test connection
            for (int i = 0; i < numberOfConnections; i++) {
                Assert.assertTrue(connections[i].isConnected());
            }
            for (int i = 0; i < numberOfConnections; i++) {
                do {
                    messages[i] = CacheMessage.parse(connections[i].getData());
                } while (messages[i] == null);
                Assert.assertEquals(data, messages[i].getData());
                Assert.assertEquals("POST", messages[i].getRequestMethod());
                Assert.assertEquals(resource, messages[i].getResource());
            }
            while (!hasToSendData) {
                for (int i = 0; i < numberOfConnections; i++) {
                    if (connections[i].hasData()) {
                        message = CacheMessage.parse(connections[i].getData());
                        if (message != null) {
                            hasToSendData = true;
                            connectionHasToSendData = i;
                            break;
                        }
                    }
                }
            }
            if (connectionHasToSendData != -1) {
                message.setData(data);
                message.setRequestMethod("POST");
                if (connections[connectionHasToSendData].isConnected()) {
                    connections[connectionHasToSendData].sendData(message.getMessage());
                }
            }
            do {
                updateMessage = UpdateMessage.parse(connections[connectionID].getData());
            } while (updateMessage == null);
            Assert.assertEquals(userID, updateMessage.getUserID());
            Assert.assertEquals(resource, updateMessage.getResource());

            // clean up
            for (int i = 0; i < numberOfConnections; i++) {
                LogoutMessage logout = new LogoutMessage();
                logout.setSender(connectionIDs[i]);
                connections[i].sendData(logout.getMessage());
                connections[i].close();
            }
            server.join();
            Assert.assertNull(server.error);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        } catch (AssertionError ae) {
            ae.printStackTrace();
            throw ae;
        }
    }

}
