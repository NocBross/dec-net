package test.java.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Test;

import main.java.connection.TCPConnection;
import main.java.constants.LogFiles;
import main.java.message.LoginMessage;
import main.java.message.ReportMessage;
import main.java.service.CustomService;
import main.java.services.LoginService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;
import test.java.TestData;

public class LoginServiceTest {

    @Test
    public void test() {
        try {
            ServerSecrets secrets = new ServerSecrets();
            secrets.loadServerSecrets();
            CustomService loginService = new LoginService(TestData.SERVER_PORT, secrets);
            loginService.start();
            Thread.sleep(50);

            // tests
            testExceptions();
            testNotRegisteredUser();
            testRegisteredUser(secrets);

            // clean up
            loginService.stopService();
            loginService.join();
            Assert.assertFalse(loginService.isAlive());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    /**
     * Tests the exceptions which are thrown by the login service.
     */
    private void testExceptions() {
        try {
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
            reader.close();
            logFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    /**
     * Tests the login service when a not registered user wants to login.
     */
    private void testNotRegisteredUser() {
        try {
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
            Assert.assertEquals("Benutzername oder Passwort falsch!", report.getErrorCode());
            Assert.assertEquals(LoginMessage.ID, report.getReferencedMessage());

            // clean up
            connection.close();
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
            DatabaseConnector database = new DatabaseConnector(secrets.getDatabaseUser(),
                    secrets.getDatabasePassword());
            database.insertNewUser(TestData.NICKNAME, TestData.PASSWORD);

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
            Assert.assertEquals(LoginMessage.ID, report.getReferencedMessage());

            // clean up
            connection.close();
            database.deleteUser(TestData.NICKNAME);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

}
