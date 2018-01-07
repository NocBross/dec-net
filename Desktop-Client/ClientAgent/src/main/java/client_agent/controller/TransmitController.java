package main.java.client_agent.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import main.java.client_agent.ClientAgent;
import main.java.client_agent.abstraction.ConnectionModel;
import main.java.client_agent.abstraction.TransmitData;
import main.java.client_agent.abstraction.TransmitModel;
import main.java.connection.TCPConnection;
import main.java.constants.Network;
import main.java.message.LoginMessage;
import main.java.service.CustomLogger;

public class TransmitController extends Thread {

    private boolean isRunning;
    private String logID;
    private ClientAgent agent;
    private Condition notEmpty;
    private ConnectionModel connectionModel;
    private Lock runLock;
    private TransmitModel transmitModel;
    private CustomLogger logger;


    public TransmitController(ClientAgent agent, ConnectionModel connectionModel, TransmitModel transmitModel,
            CustomLogger logger) {
        isRunning = false;
        logID = "TransmitController";
        runLock = new ReentrantLock();
        notEmpty = runLock.newCondition();
        this.agent = agent;
        this.connectionModel = connectionModel;
        this.transmitModel = transmitModel;
        this.logger = logger;
    }


    /**
     * Adds a new package to the package list and send a signal to the
     * controller for wake up.
     * 
     * @param packet
     *        - package with message and recipient
     */
    public void addMessage(String endpoint, String message) {
        logger.writeLog(logID + " added new message to send queue", null);
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
                    logger.writeLog(logID + " by waiting", ie);
                }
            } else {
                message = transmitModel.getMessage();
                connectionModel.lockServerConnection();
                if(message.getDestination().equals(Network.NETWORK_HUB)) {
                    logger.writeLog(logID + " sending message to hub ", null);
                    sendToHub(message.getDestination(), message.getMessage());
                } else {
                    logger.writeLog(logID + " sending message as request", null);
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
        (new HttpController(agent, url, message, logger)).start();
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
    private void sendToHub(String recipient, String message) {
        try {
            TCPConnection connection = connectionModel.getServerConnection();
            if(connection == null || !connection.isConnected()) {
                LoginMessage loginMessage = LoginMessage.parse(message);
                String address = loginMessage.getNickname().split("@")[1];
                connectionModel.setServerAddress(InetAddress.getByName(address));
                connectionModel.addHubConnection(recipient);
            }

            connectionModel.getServerConnection().sendData(message);
        } catch(UnknownHostException uhe) {
            logger.writeLog(logID + " error while sending to hub", uhe);
        } catch(Exception e) {
            logger.writeLog(logID + " error while sending to hub", e);
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
