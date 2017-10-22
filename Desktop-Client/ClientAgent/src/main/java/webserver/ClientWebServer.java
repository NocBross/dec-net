package main.java.webserver;

import java.io.File;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class ClientWebServer {

    private File contextRoot;
    private HttpServer server;


    public ClientWebServer(int port) throws Exception {
        contextRoot = new File("context");
        if( !contextRoot.exists()) {
            contextRoot.mkdir();
        }

        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new ContextHandler(contextRoot.getName()));
    }


    public int getPort() {
        return server.getAddress().getPort();
    }


    public void start() {
        server.start();
    }


    public void stop() {
        server.stop(0);
    }

}
