package main.java.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONObject;
import org.json.JSONTokener;

import main.java.connection.TCPConnection;
import main.java.constants.LogFiles;
import main.java.service.CustomService;

public class ShippingService extends CustomService {

    private Iterator<String> connectionIterator;
    private Map<String, TCPConnection> connections;
    private Lock connectionLock;


    public ShippingService() throws IOException {
        super( -1, LogFiles.SHIPPING_LOG);

        connectionIterator = null;
        connections = new HashMap<String, TCPConnection>();
        connectionLock = new ReentrantLock();
    }


    /**
     * Adds a new connection to the service.
     * 
     * @param nickname
     *        - hashed mail address of the user
     * @param newClient
     *        - socket of the connection
     * @return true in case of success<br>
     *         false in case of the user is already connected
     */
    public boolean addConnection(String nickname, TCPConnection newConnection) {
        boolean result = false;
        connectionLock.lock();

        connections.put(nickname, newConnection);
        if(connections.get(nickname) != null) {
            result = true;
        }

        connectionLock.unlock();
        return result;
    }


    @Override
    protected void service() {
        String key = "";

        connectionLock.lock();
        connectionIterator = connections.keySet().iterator();
        while(connectionIterator.hasNext()) {
            key = connectionIterator.next();
            try {
                if(connections.get(key).hasData()) {
                    try {
                        String message = connections.get(key).getData();
                        while(message.equals("1") && connections.get(key).hasData()) {
                            message = connections.get(key).getData();
                        }

                        if( !message.equals("1")) {
                            JSONTokener parser = new JSONTokener(message);
                            JSONObject jsonMessage = (JSONObject) parser.nextValue();

                            switch(jsonMessage.getString("type")) {
                                default:
                                    break;
                            }
                        }
                    } catch(ClassCastException cce) {
                        logger.writeLog("cannot parse incoming message to JSON", cce);
                    } catch(ClassNotFoundException cnfe) {
                        logger.writeLog("cannot parse incoming data to string datatype", cnfe);
                    }
                }
            } catch(IOException ioe) {
                deleteConnection(key, connectionIterator);
                logger.writeLog("cannot read from inputstream", ioe);
            } catch(SQLException sqle) {
                logger.writeLog("databse access error or closed result set", sqle);
            } catch(Exception e) {
                logger.writeLog("unexpected exception" + System.lineSeparator(), e);
            }
        }
        connectionLock.unlock();
    }


    /**
     * Closes and deletes the connection which is identified by the key.
     * 
     * @param key
     *        - hashed mail of the user
     * @param iterator
     *        - iterator over the connection list
     * @throws IOException
     *         - if an I/O error occurs
     */
    private void deleteConnection(String key, Iterator<String> iterator) {
        try {
            connections.get(key).close();
            iterator.remove();
        } catch(IOException ioe) {
            logger.writeLog("cannot delete connection", ioe);
        }
    }
}
