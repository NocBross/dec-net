package main.java.client_agent.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import main.java.client_agent.ClientAgent;
import main.java.service.CustomLogger;

public class HttpController extends Thread {

    private CustomLogger logger;
    private ClientAgent agent;
    private String logID;
    private String message;
    private String urlString;


    public HttpController(ClientAgent agent, String url, String message, CustomLogger logger) {
        this.agent = agent;
        logID = "HttpController";
        this.message = message;
        this.urlString = url;
        this.logger = logger;
    }


    @Override
    public void run() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            if(message == null) {
                logger.writeLog(logID + " sending GET-Request for " + urlString, null);
                connection.setRequestMethod("GET");
            } else {
                logger.writeLog(logID + " sending POST_Request for " + urlString, null);
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

            if(connection.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                for(String line; (line = reader.readLine()) != null;) {
                    agent.updateMessage(line);
                }
                reader.close();
            }
        } catch(IOException ioe) {
            logger.writeLog(logID + " error while sending", ioe);
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch(Exception e) {
                    logger.writeLog(logID + " cannot close reader", e);
                }
            }

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

}
