package main.java.client_agent.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import main.java.client_agent.ClientAgent;
import main.java.client_agent.abstraction.ConnectionModel;
import main.java.connection.TCPConnection;

public class ReceiveController extends Thread {

    private boolean running;
    private String message;
    private ClientAgent agent;
    private Lock runLock;
    private ConnectionModel connectionModel;

    public ReceiveController(ConnectionModel connectionModel, ClientAgent clientAgent) {
        agent = clientAgent;
        running = true;
        message = null;
        runLock = new ReentrantLock();
        this.connectionModel = connectionModel;
    }

    @Override
    public void run() {
        runLock.lock();
        while (running) {
            runLock.unlock();

            receiveFromServer();

            runLock.lock();
        }
        runLock.unlock();
    }

    /**
     * Receives message from the server.
     */
    private void receiveFromServer() {
        message = null;
        TCPConnection connection = connectionModel.getServerConnection();

        if (connection != null) {
            try {
                do {
                    message = connection.getData();
                } while (message.equals("1"));

                agent.updateMessage(message);
            } catch (SocketTimeoutException ste) {

            } catch (Exception e) {
            }
        }

        connectionModel.lockHTTPConnection();
        Iterator<HttpURLConnection> httpConnectionIterator = connectionModel.getHTTPConnections().iterator();
        while (httpConnectionIterator.hasNext()) {
            try {
                HttpURLConnection httpConnection = httpConnectionIterator.next();
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                for (String line; (line = reader.readLine()) != null;) {
                    agent.updateMessage(line);
                }
                reader.close();
                httpConnectionIterator.remove();
            } catch (Exception e) {

            }
        }
        connectionModel.unlockHTTPConnection();
    }

    /**
     * Stops the transmit controller.
     */
    public void stopController() {
        runLock.lock();
        running = false;
        runLock.unlock();
    }
}
