package main.java.client_agent.controller;

import java.io.EOFException;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import main.java.client_agent.ClientAgent;
import main.java.client_agent.abstraction.ConnectionModel;
import main.java.connection.TCPConnection;
import main.java.service.CustomLogger;

public class ReceiveController extends Thread {

    private boolean running;
    private String logID;
    private String message;
    private ClientAgent agent;
    private Lock runLock;
    private ConnectionModel connectionModel;
    private CustomLogger logger;


    public ReceiveController(ConnectionModel connectionModel, ClientAgent clientAgent, CustomLogger logger) {
        agent = clientAgent;
        running = true;
        logID = "ReceiveController";
        message = null;
        runLock = new ReentrantLock();
        this.connectionModel = connectionModel;
        this.logger = logger;
    }


    @Override
    public void run() {
        runLock.lock();
        while(running) {
            runLock.unlock();

            receiveFromServer();

            runLock.lock();
        }
        runLock.unlock();
    }


    /**
     * Stops the transmit controller.
     */
    public void stopController() {
        runLock.lock();
        running = false;
        runLock.unlock();
    }


    /**
     * Receives message from the server.
     */
    private void receiveFromServer() {
        message = null;
        TCPConnection connection = connectionModel.getServerConnection();
        // logger.writeLog(logID + " waiting for new message", null);

        if(connection != null) {
            try {
                do {
                    message = connection.getData();
                } while(message.equals("1"));

                agent.updateMessage(message);
            } catch(SocketTimeoutException ste) {

            } catch(EOFException eofe) {
                logger.writeLog(logID + " connection closed by server", eofe);
                try {
                    connection.close();
                } catch(Exception e) {
                    logger.writeLog(logID + " cannot close connection", e);
                }
            } catch(Exception e) {
                logger.writeLog(logID + " error while waiting for new message", e);
            }
        }
    }
}
