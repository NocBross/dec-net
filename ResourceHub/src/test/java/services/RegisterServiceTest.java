package test.java.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Test;

import main.java.connection.TCPConnection;
import main.java.constants.LogFiles;
import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;
import main.java.service.CustomService;
import main.java.services.RegisterService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;
import test.java.TestData;

public class RegisterServiceTest {

    // attributes
    // end of attributes

    // constructor
    // end of constructor

    // methods
    @Test
    public void test() {
        try {
            ServerSecrets secrets = new ServerSecrets();
            secrets.loadServerSecrets();
            CustomService registerService = new RegisterService(TestData.SERVER_PORT, secrets);
            registerService.start();
            Thread.sleep(50);

            // tests
            testCorrectRegister(secrets);
            testExceptions();
            testIncorrectRegister(secrets);

            // clean up
            registerService.stopService();
            registerService.join();
            Assert.assertFalse(registerService.isAlive());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    } // end of test

    /**
     * Test the register service when the register process terminated correctly.
     */
    private void testCorrectRegister(ServerSecrets secrets) {
        try {
            // initialize test
            DatabaseConnector database = new DatabaseConnector(secrets.getDatabaseUser(),
                    secrets.getDatabasePassword());

            // run test
            TCPConnection connection = new TCPConnection(InetAddress.getByName("localhost"), TestData.SERVER_PORT);
            RegisterMessage message = new RegisterMessage();
            message.setNickname(TestData.NICKNAME);
            message.setPassword(TestData.PASSWORD);
            connection.sendData(message.getMessage());

            ReportMessage report = null;
            do {
                report = ReportMessage.parse(connection.getData());
            } while (report == null);

            Assert.assertTrue(report.getResult());
            Assert.assertEquals(RegisterMessage.ID, report.getReferencedMessage());

            // clean up
            database.deleteUser(TestData.NICKNAME);
            database.closeConnection();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    } // end of testCorrectRegister

    /**
     * Tests the exceptions which are thrown by the register service.
     */
    private void testExceptions() {
        try {
            // run test
            TCPConnection connection = new TCPConnection(InetAddress.getByName("localhost"), TestData.SERVER_PORT);
            connection.sendData("1");
            connection.close();

            Thread.sleep(1000);

            File logFile = new File(LogFiles.REGISTER_LOG);
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
    } // end of testExceptions

    /**
     * Test the register service when the register process terminated not correctly.
     */
    private void testIncorrectRegister(ServerSecrets secrets) {
        try {
            // initialize test
            DatabaseConnector database = new DatabaseConnector(secrets.getDatabaseUser(),
                    secrets.getDatabasePassword());
            database.insertNewUser(TestData.NICKNAME, TestData.PASSWORD);

            // run test with stored mail which is not hashed
            TCPConnection connection = new TCPConnection(InetAddress.getByName("localhost"), TestData.SERVER_PORT);
            RegisterMessage message = new RegisterMessage();
            message.setNickname(TestData.NICKNAME);
            message.setPassword(TestData.PASSWORD);
            connection.sendData(message.getMessage());

            ReportMessage report = null;
            do {
                report = ReportMessage.parse(connection.getData());
            } while (report == null);

            Assert.assertFalse(report.getResult());
            Assert.assertEquals("Benutzername bereits vorhanden", report.getErrorCode());
            Assert.assertEquals(RegisterMessage.ID, report.getReferencedMessage());

            // run test with stored mail which is hashed
            connection.close();
            connection = new TCPConnection(InetAddress.getByName("localhost"), TestData.SERVER_PORT);
            connection.sendData(message.getMessage());

            report = null;
            do {
                report = ReportMessage.parse(connection.getData());
            } while (report == null);

            Assert.assertFalse(report.getResult());
            Assert.assertEquals("Benutzername bereits vorhanden", report.getErrorCode());
            Assert.assertEquals(RegisterMessage.ID, report.getReferencedMessage());

            // clean up
            database.deleteUser(TestData.NICKNAME);
            database.closeConnection();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    } // end of testIncorrectRegister
      // end of methods
} // end of class
