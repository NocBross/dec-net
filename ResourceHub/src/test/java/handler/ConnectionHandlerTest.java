package test.java.handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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

import main.java.connection.IPAddressData;
import main.java.constants.WebServiceConstants;
import main.java.constants.WebServiceContext;
import main.java.message.AddressMessage;
import main.java.services.ShippingService;
import main.java.services.WebService;
import main.java.util.ServerSecrets;

public class ConnectionHandlerTest {

    @Test
    public void test() {
        try {
            int port = 26345;
            ServerSecrets secrets = new ServerSecrets();
            secrets.loadServerSecrets();

            // test
            testBadRequest(port, secrets);
            testRequestWithoutData(port, secrets);
            testRequestWithData(port, secrets);
            testPOSTNoData(port, secrets);
            testPOSTData(port, secrets);
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
        String request = "http://localhost:" + port + WebServiceContext.CONNECTION;
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

    private void testRequestWithoutData(int port, ServerSecrets secrets) throws Exception {
        String userID = "user_1@localhost";
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.CONNECTION
                + WebServiceConstants.CONTEXT_SEPARATOR + WebServiceConstants.USER_ID_KEY
                + WebServiceConstants.KEY_VALUE_SEPARATOR + userID.split("@")[0];
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);

        List<String> userSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<IPAddressData>() {

            @Override
            public IPAddressData answer(InvocationOnMock invocation) throws Throwable {
                userSpy.add((String) invocation.getArguments()[0]);
                return null;
            }

        }).when(shippingService).getAddressData(Mockito.anyString());

        service.startService();

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assert.assertEquals(404, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        Assert.assertEquals("<b>NOT FOUND</b>", response);
        Assert.assertEquals(1, userSpy.size());
        Assert.assertEquals(userID.split("@")[0], userSpy.get(0));

        service.stopService();
    }

    private void testRequestWithData(int port, ServerSecrets secrets) throws Exception {
        String userID = "user_1@localhost";
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.CONNECTION
                + WebServiceConstants.CONTEXT_SEPARATOR + WebServiceConstants.USER_ID_KEY
                + WebServiceConstants.KEY_VALUE_SEPARATOR + userID.split("@")[0];
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);
        IPAddressData addressData = new IPAddressData();

        List<String> userSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<IPAddressData>() {

            @Override
            public IPAddressData answer(InvocationOnMock invocation) throws Throwable {
                userSpy.add((String) invocation.getArguments()[0]);
                return addressData;
            }

        }).when(shippingService).getAddressData(Mockito.anyString());
        addressData.setActiveAddress("127.0.0.1");
        addressData.setExternalAddress("127.0.0.1");
        addressData.setLocalAddress("127.0.0.1");
        addressData.setActivePort(15200);
        addressData.setExternalPort(15200);
        addressData.setLocalPort(15200);

        service.startService();

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assert.assertEquals(200, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        for (String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        AddressMessage message = AddressMessage.parse(response);
        Assert.assertNotNull(message);
        Assert.assertEquals(addressData.getExternalAddress(), message.getExternalAddress());
        Assert.assertEquals(addressData.getLocalAddress(), message.getLocalAddress());
        Assert.assertEquals(addressData.getExternalPort(), message.getExternalPort());
        Assert.assertEquals(addressData.getLocalPort(), message.getLocalPort());
        Assert.assertEquals(1, userSpy.size());
        Assert.assertEquals(userID.split("@")[0], userSpy.get(0));

        service.stopService();
        reader.close();
    }

    private void testPOSTNoData(int port, ServerSecrets secrets) throws Exception {
        String userID = "user_1@localhost";
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.CONNECTION
                + WebServiceConstants.CONTEXT_SEPARATOR + WebServiceConstants.USER_ID_KEY
                + WebServiceConstants.KEY_VALUE_SEPARATOR + userID.split("@")[0];
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);

        service.startService();

        byte[] binaryMessage = userID.getBytes(StandardCharsets.UTF_8);
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

    private void testPOSTData(int port, ServerSecrets secrets) throws Exception {
        String userID = "user_1@localhost";
        String response = "";
        String request = "http://localhost:" + port + WebServiceContext.CONNECTION
                + WebServiceConstants.CONTEXT_SEPARATOR + WebServiceConstants.USER_ID_KEY
                + WebServiceConstants.KEY_VALUE_SEPARATOR + userID.split("@")[0];
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);
        AddressMessage message = new AddressMessage();

        List<AddressMessage> addressSpy = new LinkedList<AddressMessage>();
        Mockito.doAnswer(new Answer<AddressMessage>() {

            @Override
            public AddressMessage answer(InvocationOnMock invocation) throws Throwable {
                addressSpy.add((AddressMessage) invocation.getArguments()[0]);
                return null;
            }

        }).when(shippingService).updateAddressData(Mockito.any());

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
        Assert.assertEquals("<b>OK</b>", response);
        Assert.assertEquals(1, addressSpy.size());
        Assert.assertNotNull(AddressMessage.parse(addressSpy.get(0).getMessage()));
        Assert.assertEquals(message.getMessage(), addressSpy.get(0).getMessage());

        service.stopService();
        reader.close();
    }

}
