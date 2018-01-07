package main.java.client_agent.abstraction;

import java.net.InetAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import main.java.connection.TCPConnection;
import main.java.constants.Network;
import main.java.service.CustomLogger;

public class ConnectionModel {

    // attributes
    private CustomLogger logger;
    private String logID;
    private InetAddress serverAddress;
    private Lock httpConnectionLock;
    private Lock serverConnectionLock;
    private TCPConnection serverConnection;
    // end of attributes


    // constructor
    public ConnectionModel(CustomLogger logger) {
        this.logger = logger;
        logID = "ConnectionModel";
        this.serverAddress = null;
        httpConnectionLock = new ReentrantLock();
        serverConnectionLock = new ReentrantLock();

        serverConnection = null;
    }
    // end of constructor


    // methods
    /**
     * Adds a new connection to the model.<br>
     * The calling method has to get serverConnectionLock first. If the calling
     * method does not hold the lock there is no guarantee for a correct
     * behavior.
     * 
     * @return true in case of success<br>
     *         false otherwise
     */
    public boolean addHubConnection(String destination) {
        boolean result = false;
        lockServerConnection();

        try {
            if(serverConnection == null && serverAddress != null) {
                serverConnection = new TCPConnection(serverAddress, Network.NETWORK_HUB_PORT);
                result = true;
            }
        } catch(Exception e) {
            logger.writeLog(logID + " error while connecting to server", e);
        }

        unlockServerConnection();
        return result;
    }


    /**
     * Deletes a specific server connection.<br>
     * Tries to get the server connection lock before connection will close.
     * 
     * @return true in case of success<br>
     *         false otherwise
     */
    public boolean deleteServerConnection() {
        boolean result = false;
        lockServerConnection();

        try {
            serverConnection.close();
            serverConnection = null;
            result = true;
        } catch(Exception e) {
            logger.writeLog(logID + " error while closing server connection", e);
        }

        unlockServerConnection();
        return result;
    }


    /**
     * Returns the TCPConnection to the given service.
     * 
     * @return TCPConnection to the server or null if no connection exists
     */
    public TCPConnection getServerConnection() {
        return serverConnection;
    }


    /**
     * Locks the server connection map for the called thread.
     */
    public void lockHTTPConnection() {
        httpConnectionLock.lock();
    }


    /**
     * Unlocks the server connection map by the called thread.
     */
    public void unlockHTTPConnection() {
        httpConnectionLock.unlock();
    }


    /**
     * Locks the server connection map for the called thread.
     */
    public void lockServerConnection() {
        serverConnectionLock.lock();
    }


    /**
     * Unlocks the server connection map by the called thread.
     */
    public void unlockServerConnection() {
        serverConnectionLock.unlock();
    }


    /**
     * Sets the server address to a new one. This function is used if the server
     * is unreachable and the server has to change.
     * 
     * @param newServerAddress
     *        - address of the new server
     */
    public void setServerAddress(InetAddress newServerAddress) {
        serverAddress = newServerAddress;
    }
}
