package main.java.services;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import main.java.constants.WebServiceContext;
import main.java.handler.RegisterHandler;
import main.java.handler.ResourceHandler;
import main.java.handler.SearchHandler;
import main.java.handler.UpdateHandler;
import main.java.util.ServerSecrets;

public class WebService {

    private HttpServer server;

    public WebService(int port, ServerSecrets secrets, ShippingService shippingService) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext(WebServiceContext.REGISTER, new RegisterHandler(secrets));
        server.createContext(WebServiceContext.RESOURCE, new ResourceHandler(shippingService));
        server.createContext(WebServiceContext.SEARCH, new SearchHandler(secrets));
        server.createContext(WebServiceContext.UPDATE, new UpdateHandler(secrets, shippingService));
    }

    public void startService() {
        server.start();
    }

    public void stopService() {
        server.stop(WebServiceContext.STOP_DELAY);
    }

}
