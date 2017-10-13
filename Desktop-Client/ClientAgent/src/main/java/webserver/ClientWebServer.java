package main.java.webserver;

import java.io.File;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class ClientWebServer extends Thread {

	private File contextRoot;
	private HttpServer server;

	public ClientWebServer(int port) throws Exception {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/");

		contextRoot = new File("context");
		if (!contextRoot.exists()) {
			contextRoot.mkdir();
		}
	}

	@Override
	public void run() {

	}

}
