package test.java.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import main.java.connection.TCPConnection;
import main.java.constants.LogFiles;
import main.java.constants.ServerStatusCodes;
import main.java.message.LoginMessage;
import main.java.message.ReportMessage;
import main.java.message.UpdateMessage;
import main.java.service.CustomService;
import main.java.services.LoginService;
import main.java.services.ShippingService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;
import test.java.TestData;

public class LoginServiceTest {

    @Test
    public void test() {
        try {
            ServerSecrets secrets = new ServerSecrets();
            secrets.loadServerSecrets();

            // test
            testExceptions(secrets);
            testNotRegisteredUser(secrets);
            testRegisteredUser(secrets);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    /**
     * Tests the exceptions which are thrown by the login service.
     */
    private void testExceptions(ServerSecrets secrets) {
        try {
            // initialize
            ShippingService shippingService = Mockito.mock(ShippingService.class);
            CustomService loginService = new LoginService(TestData.SERVER_PORT, secrets, shippingService);
            shippingService.start();
            loginService.start();
            Thread.sleep(50);

            // run test
            TCPConnection connection = new TCPConnection(InetAddress.getByName("localhost"), TestData.SERVER_PORT);
            connection.sendData("1");
            connection.close();

            Thread.sleep(1000);

            File logFile = new File(LogFiles.LOGIN_LOG);
            BufferedReader reader = new BufferedReader(new FileReader(logFile));
            String line = reader.readLine();
            Assert.assertNotEquals(-1, line.indexOf("something is wrong with the streams or the incoming message"));
            line = reader.readLine();
            Assert.assertEquals("java.io.EOFException", line);

            connection = new TCPConnection(InetAddress.getByName("localhost"), TestData.SERVER_PORT);
            Thread.sleep(32000);
            connection.close();

            // clean up
            loginService.stopService();
            reader.close();
            logFile.delete();
            loginService.join();
            Assert.assertFalse(loginService.isAlive());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    /**
     * Tests the login service when a not registered user wants to login.
     */
    private void testNotRegisteredUser(ServerSecrets secrets) {
        try {
            // initialize
            ShippingService shippingService = Mockito.mock(ShippingService.class);
            CustomService loginService = new LoginService(TestData.SERVER_PORT, secrets, shippingService);
            shippingService.start();
            loginService.start();
            Thread.sleep(50);

            // run test
            TCPConnection connection = new TCPConnection(InetAddress.getByName("localhost"), TestData.SERVER_PORT);
            LoginMessage message = new LoginMessage();
            message.setNickname(TestData.NICKNAME);
            message.setPassword(TestData.PASSWORD);
            connection.sendData(message.getMessage());

            ReportMessage report = null;
            do {
                report = ReportMessage.parse(connection.getData());
            } while (report == null);

            Assert.assertFalse(report.getResult());
            Assert.assertEquals(ServerStatusCodes.LOGIN_UNKNOWN_USER_ID, report.getStatusCode());
            Assert.assertEquals(LoginMessage.ID, report.getReferencedMessage());

            // clean up
            loginService.stopService();
            connection.close();
            loginService.join();
            Assert.assertFalse(loginService.isAlive());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    /**
     * Tests the login service when a registered user wants to login.
     */
    private void testRegisteredUser(ServerSecrets secrets) {
        try {
            // initialize test
            String resource1 = "/test";
            String resource2 = "/test_test";
            UpdateMessage message1 = new UpdateMessage();
            message1.setUserID(TestData.NICKNAME);
            message1.setResource(resource1);
            UpdateMessage message2 = new UpdateMessage();
            message2.setUserID(TestData.NICKNAME);
            message2.setResource(resource2);

            DatabaseConnector database = new DatabaseConnector(secrets.getDatabaseUser(),
                    secrets.getDatabasePassword());
            database.insertNewUser(TestData.NICKNAME, TestData.PASSWORD);
            database.insertUpdateMessage(TestData.NICKNAME, message1.getMessage());
            database.insertUpdateMessage(TestData.NICKNAME, message2.getMessage());

            ShippingService shippingService = Mockito.mock(ShippingService.class);

            List<String> userIDSpy = new LinkedList<String>();
            List<TCPConnection> connectionSpy = new LinkedList<TCPConnection>();
            Mockito.doAnswer(new Answer<String>() {

                @Override
                public String answer(InvocationOnMock invocation) throws Throwable {
                    userIDSpy.add((String) invocation.getArguments()[0]);
                    connectionSpy.add((TCPConnection) invocation.getArguments()[1]);
                    return null;
                }

            }).when(shippingService).addConnection(Mockito.anyString(), Mockito.any());

            List<UpdateMessage> messageSpy = new LinkedList<UpdateMessage>();
            Mockito.doAnswer(new Answer<Boolean>() {

                @Override
                public Boolean answer(InvocationOnMock invocation) throws Throwable {
                    UpdateMessage message = (UpdateMessage) invocation.getArguments()[1];
                    userIDSpy.add((String) invocation.getArguments()[0]);
                    messageSpy.add(message);

                    if (message.getMessage().equals(message1.getMessage())) {
                        return false;
                    } else {
                        return true;
                    }
                }

            }).when(shippingService).sendUpdate(Mockito.anyString(), Mockito.any());

            CustomService loginService = new LoginService(TestData.SERVER_PORT, secrets, shippingService);
            shippingService.start();
            loginService.start();
            Thread.sleep(50);

            // run test
            TCPConnection connection = new TCPConnection(InetAddress.getByName("localhost"), TestData.SERVER_PORT);
            LoginMessage message = new LoginMessage();
            message.setNickname(TestData.NICKNAME);
            message.setPassword(TestData.PASSWORD);
            connection.sendData(message.getMessage());

            ReportMessage report = null;
            do {
                report = ReportMessage.parse(connection.getData());
            } while (report == null);
            Assert.assertTrue(report.getResult());
            Assert.assertEquals(ServerStatusCodes.LOGIN_CORRECT, report.getStatusCode());
            Assert.assertEquals(LoginMessage.ID, report.getReferencedMessage());

            Thread.sleep(2000);
            List<String> messages = database.readUpdateMessages(TestData.NICKNAME);
            Assert.assertEquals(1, messages.size());
            Assert.assertEquals(message1.getMessage(), messages.get(0));

            // clean up
            loginService.stopService();
            connection.close();
            database.deleteUser(TestData.NICKNAME);
            loginService.join();
            Assert.assertFalse(loginService.isAlive());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

}
