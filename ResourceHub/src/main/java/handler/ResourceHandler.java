package main.java.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.java.constants.LogFiles;
import main.java.constants.Network;
import main.java.message.RDFMessage;
import main.java.service.CustomLogger;
import main.java.services.ShippingService;

public class ResourceHandler implements HttpHandler {

    private CustomLogger logger;
    private ShippingService shippingService;
    private String logID;

    public ResourceHandler(ShippingService shippingService) throws Exception {
        logger = new CustomLogger(LogFiles.RESOURCE_LOG);
        this.shippingService = shippingService;
        logID = "ResourceService";
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.writeLog(logID + " new resource request received", null);
        String requestMethod = httpExchange.getRequestMethod();

        if (requestMethod.equals("GET")) {
            logger.writeLog(logID + " received GET-Request from " + httpExchange.getRemoteAddress().getHostName(),
                    null);
            handleGET(httpExchange);
        } else if (requestMethod.equals("POST")) {
            logger.writeLog(logID + " received POST-Request from " + httpExchange.getRemoteAddress().getHostName(),
                    null);
            handlePOST(httpExchange);
        }

        httpExchange.close();
    }

    private void handleGET(HttpExchange httpExchange) throws IOException {
        int status = 404;
        String host = httpExchange.getRequestHeaders().getFirst("Host");
        host = host.substring(0, host.indexOf(":"));
        String url = host + httpExchange.getRequestURI().toString();

        logger.writeLog(logID + " getting resource " + url + " for " + httpExchange.getRemoteAddress().getHostName(),
                null);
        String response = shippingService.getResource(url);

        if (response != null) {
            logger.writeLog(logID + " resource " + url + " found", null);
            status = 200;
        } else {
            logger.writeLog(logID + " resource " + url + " not found", null);
            response = "<b>NOT FOUND</b>";
        }

        logger.writeLog(
                logID + " sending resource content of " + url + " to " + httpExchange.getRemoteAddress().getHostName(),
                null);
        httpExchange.getResponseHeaders().add("Content-type", "text/html");
        httpExchange.sendResponseHeaders(status, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handlePOST(HttpExchange httpExchange) throws IOException {
        int status = 400;
        String line = "";
        String rawMessage = "";
        String response = "<b>BAD REQUEST</b>";
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
        RDFMessage message = null;

        while ((line = reader.readLine()) != null) {
            rawMessage += line;
        }
        message = RDFMessage.parse(rawMessage);

        logger.writeLog(logID + " updating resource " + message.getResourceID() + " from "
                + httpExchange.getRemoteAddress().getHostName(), null);

        if (message != null) {
            logger.writeLog(logID + " transfer resource " + message.getResourceID() + " to ShippingService", null);
            shippingService.addShippingPackage(message.getMessage(),
                    message.getResourceID().replace(String.valueOf(Network.SERVER_WEBSERVICE_PORT), ""));
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
