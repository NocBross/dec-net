package main.java.client_agent.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import main.java.client_agent.abstraction.ConnectionModel;
import main.java.client_agent.abstraction.TransmitData;
import main.java.client_agent.abstraction.TransmitModel;
import main.java.connection.TCPConnection;
import main.java.constants.EndPoint;

public class TransmitController extends Thread {

    private boolean isRunning;
    private Condition notEmpty;
    private ConnectionModel connectionModel;
    private Lock runLock;
    private TransmitModel transmitModel;


    public TransmitController(ConnectionModel connectionModel, TransmitModel transmitModel) {
        isRunning = false;
        runLock = new ReentrantLock();
        notEmpty = runLock.newCondition();
        this.connectionModel = connectionModel;
        this.transmitModel = transmitModel;
    }


    /**
     * Adds a new package to the package list and send a signal to the
     * controller for wake up.
     * 
     * @param packet
     *        - package with message and recipient
     */
    public void addMessage(String endpoint, String message) {
        transmitModel.addMessage(new TransmitData(endpoint, message));
        wakeUp();
    }


    /**
     * Sends the packages in the package queue.
     */
    @Override
    public void run() {
        TransmitData message = null;
        runLock.lock();

        while(isRunning) {
            runLock.unlock();

            if(transmitModel.isMessageQueueEmpty()) {
                try {
                    runLock.lock();
                    notEmpty.await();
                    runLock.unlock();
                } catch(InterruptedException ie) {
                    ie.printStackTrace();
                }
            } else {
                message = transmitModel.getMessage();
                connectionModel.lockServerConnection();
                if(message.getDestination().indexOf(EndPoint.SERVICE_PREFIX) != -1) {
                    sendToServer(message.getDestination(), message.getMessage());
                } else {
                    sendRequest(message.getDestination(), message.getMessage());
                }
                connectionModel.unlockServerConnection();
            }

            runLock.lock();
        }

        runLock.unlock();
    }


    @Override
    public void start() {
        runLock.lock();
        isRunning = true;
        runLock.unlock();

        super.start();
    }


    /**
     * Stops the controller thread.
     */
    public void stopController() {
        runLock.lock();
        isRunning = false;
        notEmpty.signal();
        runLock.unlock();
    }


    /**
     * Sends the given message to specified service and crates a new connection
     * if the client is not connected with this specific service.
     * 
     * @param recipient
     *        - name of the service
     * @param message
     *        - message which hash to send
     */
    private void sendRequest(String url, String message) {
        connectionModel.lockHTTPConnection();
        try {
            HttpURLConnection connection = connectionModel.addHTTPConnection(url);

            if(message == null) {
                connection.setRequestMethod("GET");
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
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        connectionModel.unlockHTTPConnection();
    }


    /**
     * Sends the given message to specified service and crates a new connection
     * if the client is not connected with this specific service.
     * 
     * @param recipient
     *        - name of the service
     * @param message
     *        - message which hash to send
     */
    private void sendToServer(String recipient, String message) {
        TCPConnection connection = connectionModel.getServerConnection();
        if(connection == null || !connection.isConnected()) {
            connectionModel.addServerConnection(recipient);
        }

        try {
            connectionModel.getServerConnection().sendData(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Restarts the controller if the send queue in the TransmitModel is not
     * empty.
     */
    private void wakeUp() {
        runLock.lock();
        notEmpty.signal();
        runLock.unlock();
    }
}
