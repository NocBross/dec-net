package main.java.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.java.services.ShippingService;

public class ResourceHandler implements HttpHandler {

    private ShippingService shippingService;

    public ResourceHandler(ShippingService shippingService) {
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
        String response = shippingService.getResource(httpExchange.getRequestURI().toString());

        if (response != null) {
            status = 200;
        } else {
            status = 404;
            response = "<b>NOT FOUND</b>";
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
        String message = "";
        String response = "<b>OK</b>";
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));

        while ((line = reader.readLine()) != null) {
            message += line;
        }
        shippingService.addShippingPackage(message, httpExchange.getRequestURI().toString());

        httpExchange.getResponseHeaders().add("Content-type", "text/html");
        httpExchange.sendResponseHeaders(status, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
