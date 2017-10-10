package main.java.client_agent.abstraction;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import main.java.connection.TCPConnection;
import main.java.constants.EndPoint;
import main.java.constants.Port;

public class ConnectionModel {

    // attributes
    private InetAddress serverAddress;
    private Lock httpConnectionLock;
    private Lock serverConnectionLock;
    private Map<String, Integer> portMap;
    private TCPConnection serverConnection;
    private List<HttpURLConnection> httpConnections;
    // end of attributes

    // constructor
    public ConnectionModel(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
        httpConnectionLock = new ReentrantLock();
        serverConnectionLock = new ReentrantLock();

        portMap = new HashMap<String, Integer>();
        portMap.put(EndPoint.LOGIN_END_POINT, Port.LOGIN_SERVICE);
        portMap.put(EndPoint.REGISTER_END_POINT, Port.REGISTER_SERVICE);

        serverConnection = null;
        httpConnections = new LinkedList<HttpURLConnection>();
    }
    // end of constructor

    // methods
    /**
     * Adds a new http connection to the model.<br>
     * The calling method has to get serverConnectionLock first. If the calling
     * method does not hold the lock there is no guarantee for a correct behavior.
     * 
     * @throws IOException
     */
    public HttpURLConnection addHTTPConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        httpConnections.add(connection);

        return connection;
    }

    /**
     * Adds a new connection to the model.<br>
     * The calling method has to get serverConnectionLock first. If the calling
     * method does not hold the lock there is no guarantee for a correct behavior.
     * 
     * @return true in case of success<br>
     *         false otherwise
     */
    public boolean addServerConnection(String destination) {
        boolean result = false;
        lockServerConnection();

        try {
            if (serverConnection == null) {
                serverConnection = new TCPConnection(serverAddress, portMap.get(destination).intValue());
                result = true;
            }
        } catch (Exception e) {
        }

        unlockServerConnection();
        return result;
    }

    /**
     * Deletes a specific server connection.
     * 
     * @param key
     *            - connection name
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
        } catch (Exception e) {
        }

        unlockServerConnection();
        return result;
    }

    public List<HttpURLConnection> getHTTPConnections() {
        return httpConnections;
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
     * Sets the server address to a new one. This function is used if the server is
     * unreachable and the server has to change.
     * 
     * @param newServerAddress
     *            - address of the new server
     */
    public void setServerAddress(InetAddress newServerAddress) {
        serverAddress = newServerAddress;
    }
}
