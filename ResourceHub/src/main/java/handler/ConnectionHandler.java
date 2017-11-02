package main.java.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.java.connection.IPAddressData;
import main.java.constants.WebServiceConstants;
import main.java.message.AddressMessage;
import main.java.services.ShippingService;

public class ConnectionHandler implements HttpHandler {

    private ShippingService shippingService;

    public ConnectionHandler(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();

        if (requestMethod.equals("GET")) {
            handleGET(httpExchange);
        } else if (requestMethod.equals("POST")) {
            handlePOST(httpExchange);
        }

        httpExchange.close();
    }

    private void handleGET(HttpExchange httpExchange) throws IOException {
        int status = 400;
        String response = "";
        String request = httpExchange.getRequestURI().toString();
        String[] connectionRequests = request.split(WebServiceConstants.CONTEXT_SEPARATOR_ESCAPED);
        AddressMessage message = null;

        if (connectionRequests.length >= 2) {
            String[] connectionRequest = connectionRequests[1].split(WebServiceConstants.KEY_VALUE_SEPARATOR);
            IPAddressData data = shippingService.getAddressData(connectionRequest[1]);

            if (data != null) {
                message = new AddressMessage();

                message.setUserID(connectionRequest[1]);
                message.setExternalAddress(data.getExternalAddress());
                message.setExternalPort(data.getExternalPort());
                message.setLocalAddress(data.getLocalAddress());
                message.setLocalPort(data.getLocalPort());

                status = 200;
                response = message.getMessage();
            } else {
                status = 404;
                response = "<b>NOT FOUND</b>";
            }
        }

        httpExchange.getResponseHeaders().add("Content-type", "text/html");
        httpExchange.sendResponseHeaders(status, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handlePOST(HttpExchange httpExchange) throws IOException {
        int status = 200;
        String line = "";
        String rawMessage = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));

        while ((line = reader.readLine()) != null) {
            rawMessage += line;
        }

        AddressMessage message = AddressMessage.parse(rawMessage);
        shippingService.updateAddressData(message);

        httpExchange.getResponseHeaders().add("Content-type", "text/html");
        httpExchange.sendResponseHeaders(status, 0);
    }

}
