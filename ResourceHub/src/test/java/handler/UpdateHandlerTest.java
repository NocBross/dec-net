package test.java.handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import main.java.constants.LogFiles;
import main.java.constants.WebServiceContext;
import main.java.message.ReportMessage;
import main.java.message.UpdateMessage;
import main.java.services.ShippingService;
import main.java.services.WebService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class UpdateHandlerTest {

    @Test
    public void test() {
        try {
            int port = 26345;
            String userID = "nick@localhost";
            ServerSecrets secrets = new ServerSecrets();
            secrets.loadServerSecrets();

            // test
            testBadRequest(port, secrets);
            testWrongPOST(port, secrets);
            testWrongMessageType(port, userID, secrets);
            testOnlineUpdate(port, userID, secrets);
            testOfflineUpdate(port, userID, secrets);

            // clean up
            File log = new File(LogFiles.UPDATE_LOG);
            for (File file : log.getParentFile().listFiles()) {
                Assert.assertTrue(file.delete());
            }
            Assert.assertTrue(log.getParentFile().delete());
        } catch (AssertionError ae) {
            ae.printStackTrace();
            throw ae;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    private void testBadRequest(int port, ServerSecrets secrets) throws Exception {
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.UPDATE;
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);
        service.startService();

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assert.assertEquals(400, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        Assert.assertEquals("<b>BAD REQUEST</b>", response);

        service.stopService();
        reader.close();
    }

    private void testWrongPOST(int port, ServerSecrets secrets) throws Exception {
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.UPDATE;
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);
        service.startService();

        byte[] binaryMessage = "test".getBytes(StandardCharsets.UTF_8);
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

        Assert.assertEquals(400, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        Assert.assertEquals("<b>BAD REQUEST</b>", response);

        service.stopService();
        reader.close();
    }

    private void testWrongMessageType(int port, String userID, ServerSecrets secrets) throws Exception {
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.UPDATE;
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);
        service.startService();
        ReportMessage report = new ReportMessage();

        byte[] binaryMessage = report.getMessage().getBytes(StandardCharsets.UTF_8);
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

        Assert.assertEquals(400, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        Assert.assertEquals("<b>BAD REQUEST</b>", response);

        service.stopService();
        reader.close();
    }

    private void testOnlineUpdate(int port, String userID, ServerSecrets secrets) throws Exception {
        String resource = "http://localhost/test";
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.UPDATE;
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);
        UpdateMessage update = new UpdateMessage();

        update.setResource(resource);
        update.setUserID(userID);

        List<String> userIDSpy = new LinkedList<String>();
        List<UpdateMessage> messageSpy = new LinkedList<UpdateMessage>();
        Mockito.doAnswer(new Answer<Boolean>() {

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                userIDSpy.add((String) invocation.getArguments()[0]);
                messageSpy.add((UpdateMessage) invocation.getArguments()[1]);
                return true;
            }

        }).when(shippingService).sendUpdate(Mockito.anyString(), Mockito.any());

        service.startService();

        byte[] binaryMessage = update.getMessage().getBytes(StandardCharsets.UTF_8);
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
        Assert.assertEquals("<b>OK</b>", response);
        Assert.assertEquals(1, userIDSpy.size());
        Assert.assertEquals(userID, userIDSpy.get(0));
        Assert.assertEquals(1, messageSpy.size());
        Assert.assertEquals(update.getMessage(), messageSpy.get(0).getMessage());

        service.stopService();
        reader.close();
    }

    private void testOfflineUpdate(int port, String userID, ServerSecrets secrets) throws Exception {
        String resource = "http://localhost/test";
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.UPDATE;
        List<String> messages = null;
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);
        DatabaseConnector database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
        database.insertNewUser(userID, "123aBc!4?D");
        UpdateMessage update = new UpdateMessage();
        update.setUserID(userID);
        update.setResource(resource);

        List<String> userIDSpy = new LinkedList<String>();
        List<UpdateMessage> messageSpy = new LinkedList<UpdateMessage>();
        Mockito.doAnswer(new Answer<Boolean>() {

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                userIDSpy.add((String) invocation.getArguments()[0]);
                messageSpy.add((UpdateMessage) invocation.getArguments()[1]);
                return false;
            }

        }).when(shippingService).sendUpdate(Mockito.anyString(), Mockito.any());

        service.startService();

        byte[] binaryMessage = update.getMessage().getBytes(StandardCharsets.UTF_8);
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
        Assert.assertEquals("<b>OK</b>", response);
        Assert.assertEquals(1, userIDSpy.size());
        Assert.assertEquals(userID, userIDSpy.get(0));
        Assert.assertEquals(1, messageSpy.size());
        Assert.assertEquals(update.getMessage(), messageSpy.get(0).getMessage());
        messages = database.readUpdateMessages(userID);
        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(update.getMessage(), messages.get(0));

        service.stopService();
        reader.close();
        database.deleteUser(userID);
        database.closeConnection();
    }

}
