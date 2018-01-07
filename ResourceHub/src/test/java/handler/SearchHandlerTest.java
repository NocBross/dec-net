package test.java.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import main.java.constants.LogFiles;
import main.java.constants.Network;
import main.java.constants.WebServiceConstants;
import main.java.constants.WebServiceContext;
import main.java.message.SearchMessage;
import main.java.services.ShippingService;
import main.java.services.WebService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class SearchHandlerTest {

    @Test
    public void test() {
        try {
            int port = Network.CLIENT_WEBSERVER_PORT;
            ServerSecrets secrets = new ServerSecrets();
            secrets.loadServerSecrets();

            // test
            testBadRequest(port, secrets);
            testWrongKey(port, secrets);
            testNotExistingUser(port, secrets);
            testExistingUser(port, secrets);

            // clean up
            File log = new File(LogFiles.SEARCH_LOG);
            for (File file : log.getParentFile().listFiles()) {
                Assert.assertTrue(file.delete());
            }
            Assert.assertTrue(log.getParentFile().delete());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    private void testBadRequest(int port, ServerSecrets secrets) throws Exception {
        String request = Network.LOCALHOST_URL + WebServiceContext.SEARCH;
        String response = "";
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);

        service.startService();

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Assert.assertEquals(400, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        response = "";
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        Assert.assertEquals("<b>BAD REQUEST</b>", response);

        service.stopService();
        reader.close();
    }

    private void testWrongKey(int port, ServerSecrets secrets) throws Exception {
        String notExistingKey = "cookies";
        String userID = "user@localhost";
        String request = Network.LOCALHOST_URL + WebServiceContext.SEARCH + WebServiceConstants.CONTEXT_SEPARATOR
                + notExistingKey + WebServiceConstants.KEY_VALUE_SEPARATOR + userID;
        String response = "";
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);

        service.startService();

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Assert.assertEquals(404, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        response = "";
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        Assert.assertEquals("<b>NOT FOUND</b>", response);

        service.stopService();
        reader.close();
    }

    private void testNotExistingUser(int port, ServerSecrets secrets) throws Exception {
        String notExistingUserID = "nickname";
        String request = Network.LOCALHOST_URL + WebServiceContext.SEARCH + WebServiceConstants.CONTEXT_SEPARATOR
                + WebServiceConstants.USER_ID_KEY + WebServiceConstants.KEY_VALUE_SEPARATOR + notExistingUserID;
        String response = "";
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);

        service.startService();

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Assert.assertEquals(200, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        response = "";
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        SearchMessage message = SearchMessage.parse(response);
        Assert.assertNotNull(message);
        Assert.assertEquals(0, message.getNicknames().size());

        service.stopService();
        reader.close();
    }

    private void testExistingUser(int port, ServerSecrets secrets) throws Exception {
        String userID = "user@localhost";
        String request = Network.LOCALHOST_URL + WebServiceContext.SEARCH + WebServiceConstants.CONTEXT_SEPARATOR
                + WebServiceConstants.USER_ID_KEY + WebServiceConstants.KEY_VALUE_SEPARATOR + userID;
        String response = "";
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);
        DatabaseConnector database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
        database.insertNewUser(userID, "123aBc!6?D");

        service.startService();

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Assert.assertEquals(200, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        SearchMessage message = SearchMessage.parse(response);
        Assert.assertNotNull(message);
        Assert.assertEquals(1, message.getNicknames().size());
        Assert.assertEquals(userID, message.getNicknames().get(0));

        service.stopService();
        reader.close();
        database.deleteUser(userID);
        database.closeConnection();
    }

}
