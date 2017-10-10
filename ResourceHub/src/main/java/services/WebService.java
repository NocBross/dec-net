package main.java.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.sun.net.httpserver.HttpServer;

import main.java.constants.WebServiceContext;
import main.java.context.SearchHandler;
import main.java.util.ServerSecrets;

public class WebService {

    private HttpServer server;

    public WebService(int port, ServerSecrets secrets) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors()));

        server.createContext(WebServiceContext.SEARCH, new SearchHandler(secrets));
    }

    public void startService() {
        server.start();
    }

    public void stopService() {
        server.stop(WebServiceContext.STOP_DELAY);
    }

}
