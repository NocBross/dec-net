package main.java.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.java.constants.WebServiceConstants;
import main.java.message.UpdateMessage;
import main.java.services.ShippingService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class UpdateHandler implements HttpHandler {

    private DatabaseConnector database;
    private ShippingService shippingService;

    public UpdateHandler(ServerSecrets secrets, ShippingService shippingService) {
        database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
        this.shippingService = shippingService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();

        if (requestMethod.equals("POST")) {
            handlePOST(httpExchange);
        } else {
            int status = 400;
            String response = "<b>BAD REQUEST</b>";
            httpExchange.getResponseHeaders().add("Content-type", "text/html");
            httpExchange.sendResponseHeaders(status, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        httpExchange.close();
    }

    private void handlePOST(HttpExchange httpExchange) throws IOException {
        int status = 400;
        String response = "<b>BAD REQUEST</b>";
        String request = httpExchange.getRequestURI().toString();
        String[] splitRequest = request.split(WebServiceConstants.CONTEXT_SEPARATOR_ESCAPED);

        if (splitRequest.length == 2) {
            String destination = splitRequest[1].split(WebServiceConstants.KEY_VALUE_SEPARATOR)[1];
            String line = "";
            String rawMessage = "";
            UpdateMessage message = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));

            while ((line = reader.readLine()) != null) {
                rawMessage += line;
            }
            message = UpdateMessage.parse(rawMessage);

            if (message != null) {
                if (!shippingService.sendUpdate(destination, message)) {
                    database.insertUpdateMessage(destination, message.getMessage());
                }
                status = 200;
                response = "<b>OK</b>";
            }
        }

        httpExchange.getResponseHeaders().add("Content-type", "text/html");
        httpExchange.sendResponseHeaders(status, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
