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

import main.java.constants.Network;
import main.java.message.RDFMessage;
import main.java.services.ShippingService;
import main.java.services.WebService;
import main.java.util.ServerSecrets;

public class ResourceHandlerTest {

    @Test
    public void test() {
        try {
            int port = 26345;
            ServerSecrets secrets = new ServerSecrets();
            secrets.loadServerSecrets();

            // test
            testNotExistingResource(port, secrets);
            testExistingResource(port, secrets);
            testPOSTResource(port, secrets);
        } catch(Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }


    private void testNotExistingResource(int port, ServerSecrets secrets) throws Exception {
        String response = "";
        String resource = "/test";
        String request = "http://localhost:" + port + resource;
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);

        List<String> resourceSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                resourceSpy.add((String) invocation.getArguments()[0]);
                return null;
            }

        }).when(shippingService).getResource(Mockito.anyString());

        service.startService();

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assert.assertEquals(404, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        for(String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        Assert.assertEquals("<b>NOT FOUND</b>", response);
        Assert.assertEquals(1, resourceSpy.size());
        Assert.assertEquals(Network.LOCALHOST_ADDRESS + resource, resourceSpy.get(0));

        service.stopService();
        reader.close();
    }


    private void testExistingResource(int port, ServerSecrets secrets) throws Exception {
        String response = "";
        String resource = "/test";
        String request = "http://localhost:" + port + resource;
        String data = "test_user";
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);

        List<String> resourceSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                resourceSpy.add((String) invocation.getArguments()[0]);
                return data;
            }

        }).when(shippingService).getResource(Mockito.anyString());

        service.startService();

        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assert.assertEquals(200, connection.getResponseCode());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        for(String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        Assert.assertEquals(data, response);
        Assert.assertEquals(1, resourceSpy.size());
        Assert.assertEquals(Network.LOCALHOST_ADDRESS + resource, resourceSpy.get(0));

        service.stopService();
        reader.close();
    }


    private void testPOSTResource(int port, ServerSecrets secrets) throws Exception {
        String response = "";
        String resource = "/test";
        String request = "http://localhost:" + port + resource;
        String data = "test_user";
        RDFMessage message = new RDFMessage("http://localhost" + resource, data);
        ShippingService shippingService = Mockito.mock(ShippingService.class);
        WebService service = new WebService(port, secrets, shippingService);

        List<String> dataSpy = new LinkedList<String>();
        List<String> resourceSpy = new LinkedList<String>();
        Mockito.doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                dataSpy.add((String) invocation.getArguments()[0]);
                resourceSpy.add((String) invocation.getArguments()[1]);
                return null;
            }

        }).when(shippingService).addShippingPackage(Mockito.anyString(), Mockito.anyString());

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
        for(String line; (line = reader.readLine()) != null;) {
            response += line;
        }
        Assert.assertEquals("<b>OK</b>", response);
        Assert.assertEquals(1, dataSpy.size());
        Assert.assertEquals(message.getMessage(), dataSpy.get(0));
        Assert.assertEquals(1, resourceSpy.size());
        Assert.assertEquals(message.getResourceID(), resourceSpy.get(0));

        service.stopService();
        reader.close();
    }

}
