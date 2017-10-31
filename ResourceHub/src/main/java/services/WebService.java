package main.java.services;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import main.java.constants.WebServiceContext;
import main.java.handler.ConnectionHandler;
import main.java.handler.RegisterHandler;
import main.java.handler.SearchHandler;
import main.java.util.ServerSecrets;

public class WebService {

    private HttpServer server;

    public WebService(int port, ServerSecrets secrets, ShippingService shippingService) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext(WebServiceContext.CONNECTION, new ConnectionHandler(shippingService));
        server.createContext(WebServiceContext.REGISTER, new RegisterHandler(secrets));
        server.createContext(WebServiceContext.SEARCH, new SearchHandler(secrets));
    }

    public void startService() {
        server.start();
    }

    public void stopService() {
        server.stop(WebServiceContext.STOP_DELAY);
    }

}
