package test.java.handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.ServerStatusCodes;
import main.java.constants.WebServiceContext;
import main.java.message.LoginMessage;
import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;
import main.java.services.WebService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class RegisterHandlerTest {

    @Test
    public void test() {
        try {
            int port = 26345;
            String userID = "user_1@localhost";
            ServerSecrets secrets = new ServerSecrets();
            secrets.loadServerSecrets();
            DatabaseConnector database = new DatabaseConnector(secrets.getDatabaseUser(),
                    secrets.getDatabasePassword());

            // test
            testWrongMessageType(port, secrets);
            testCorrectRegister(port, userID, secrets);
            testKnwonUser(port, userID, secrets);

            // clean up
            database.deleteUser(userID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    private void testWrongMessageType(int port, ServerSecrets secrets) throws Exception {
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.REGISTER;
        WebService service = new WebService(port, secrets, null);
        LoginMessage message = new LoginMessage();
        service.startService();

        byte[] binaryMessage = message.getMessage().getBytes(StandardCharsets.UTF_8);
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", Integer.toString(binaryMessage.length));
        connection.setUseCaches(false);

        DataOutputStream dataWriter = new DataOutputStream(connection.getOutputStream());
        dataWriter.write(binaryMessage);
        dataWriter.close();

        Assert.assertEquals(200, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        ReportMessage report = ReportMessage.parse(response);
        Assert.assertNotNull(report);
        Assert.assertFalse(report.getResult());
        Assert.assertEquals(RegisterMessage.ID, report.getReferencedMessage());
        Assert.assertEquals(ServerStatusCodes.WRONG_MESSAGE_TYPE, report.getStatusCode());

        service.stopService();
        reader.close();
    }

    private void testCorrectRegister(int port, String userID, ServerSecrets secrets) throws Exception {
        String password = "123Abc!4J?";
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.REGISTER;
        WebService service = new WebService(port, secrets, null);
        RegisterMessage message = new RegisterMessage();
        service.startService();

        message.setUserID(userID);
        Assert.assertTrue(message.setPassword(password));

        byte[] binaryMessage = message.getMessage().getBytes(StandardCharsets.UTF_8);
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", Integer.toString(binaryMessage.length));
        connection.setUseCaches(false);

        DataOutputStream dataWriter = new DataOutputStream(connection.getOutputStream());
        dataWriter.write(binaryMessage);
        dataWriter.close();

        Assert.assertEquals(200, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        ReportMessage report = ReportMessage.parse(response);
        Assert.assertNotNull(report);
        Assert.assertTrue(report.getResult());
        Assert.assertEquals(RegisterMessage.ID, report.getReferencedMessage());
        Assert.assertEquals(ServerStatusCodes.REGISTER_CORRECT, report.getStatusCode());

        service.stopService();
        reader.close();
    }

    private void testKnwonUser(int port, String userID, ServerSecrets secrets) throws Exception {
        String password = "123Abc!4J?";
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.REGISTER;
        WebService service = new WebService(port, secrets, null);
        RegisterMessage message = new RegisterMessage();
        service.startService();

        message.setUserID(userID);
        Assert.assertTrue(message.setPassword(password));

        byte[] binaryMessage = message.getMessage().getBytes(StandardCharsets.UTF_8);
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", Integer.toString(binaryMessage.length));
        connection.setUseCaches(false);

        DataOutputStream dataWriter = new DataOutputStream(connection.getOutputStream());
        dataWriter.write(binaryMessage);
        dataWriter.close();

        Assert.assertEquals(200, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        ReportMessage report = ReportMessage.parse(response);
        Assert.assertNotNull(report);
        Assert.assertFalse(report.getResult());
        Assert.assertEquals(RegisterMessage.ID, report.getReferencedMessage());
        Assert.assertEquals(ServerStatusCodes.REGISTER_KNOWN_USER_ID, report.getStatusCode());

        service.stopService();
        reader.close();
    }

}
