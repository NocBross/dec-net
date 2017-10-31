package main.java.client_agent.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import main.java.client_agent.ClientAgent;
import main.java.client_agent.abstraction.ConnectionModel;

public class HttpController extends Thread {

    private ClientAgent agent;
    private ConnectionModel connectionModel;
    private String message;
    private String url;

    public HttpController(ClientAgent agent, ConnectionModel connectionModel, String url, String message) {
        this.agent = agent;
        this.connectionModel = connectionModel;
        this.message = message;
        this.url = url;
    }

    @Override
    public void run() {
        try {
            connectionModel.lockHTTPConnection();
            HttpURLConnection connection = connectionModel.addHTTPConnection(url);

            if (message == null) {
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    for (String line; (line = reader.readLine()) != null;) {
                        agent.updateMessage(line);
                    }
                    reader.close();
                }
            } else {
                byte[] binaryMessage = message.getBytes(StandardCharsets.UTF_8);

                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", Integer.toString(binaryMessage.length));
                connection.setUseCaches(false);

                DataOutputStream dataWriter = new DataOutputStream(connection.getOutputStream());
                dataWriter.write(binaryMessage);
                dataWriter.close();

                connection.getResponseCode();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            connectionModel.unlockHTTPConnection();
        }
    }

}
