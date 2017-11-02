package main.java.client_agent.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import main.java.client_agent.ClientAgent;

public class HttpController extends Thread {

    private ClientAgent agent;
    private String message;
    private String urlString;

    public HttpController(ClientAgent agent, String url, String message) {
        this.agent = agent;
        this.message = message;
        this.urlString = url;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            if (message == null) {
                connection.setRequestMethod("GET");
            } else {
                byte[] binaryMessage = message.getBytes(StandardCharsets.UTF_8);

                connection.setDoInput(true);
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
            }

            if (connection.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                for (String line; (line = reader.readLine()) != null;) {
                    agent.updateMessage(line);
                }
                reader.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
