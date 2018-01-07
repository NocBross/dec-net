package main.java.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.java.constants.LogFiles;
import main.java.message.UpdateMessage;
import main.java.service.CustomLogger;
import main.java.services.ShippingService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class UpdateHandler implements HttpHandler {

    private CustomLogger logger;
    private DatabaseConnector database;
    private ShippingService shippingService;
    private String logID;

    public UpdateHandler(ServerSecrets secrets, ShippingService shippingService) throws Exception {
        logger = new CustomLogger(LogFiles.UPDATE_LOG);
        database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
        this.shippingService = shippingService;
        logID = "UpdateService";
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.writeLog(logID + " new update request received", null);
        String requestMethod = httpExchange.getRequestMethod();

        if (requestMethod.equals("POST")) {
            logger.writeLog(logID + " received POST-Request from " + httpExchange.getRemoteAddress().getHostName(),
                    null);
            handlePOST(httpExchange);
        } else {
            logger.writeLog(
                    logID + " received wrong request method from " + httpExchange.getRemoteAddress().getHostName(),
                    null);
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
        String line = "";
        String rawMessage = "";
        UpdateMessage message = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));

        logger.writeLog(logID + " reading post content", null);
        while ((line = reader.readLine()) != null) {
            rawMessage += line;
        }
        message = UpdateMessage.parse(rawMessage);

        if (message != null) {
            logger.writeLog(logID + " sending UpdateMessage to " + message.getUserID(), null);
            if (!shippingService.sendUpdate(message.getUserID(), message)) {
                logger.writeLog(
                        logID + " user " + message.getUserID() + " is not online. Storing UpdateMessage in database.",
                        null);
                database.insertUpdateMessage(message.getUserID(), message.getMessage());
            }
            status = 200;
            response = "<b>OK</b>";
        }

        logger.writeLog(logID + " sending response to " + httpExchange.getRemoteAddress().getHostName(), null);
        httpExchange.getResponseHeaders().add("Content-type", "text/html");
        httpExchange.sendResponseHeaders(status, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
