package main.java.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONObject;
import org.json.JSONTokener;

import main.java.connection.TCPConnection;
import main.java.constants.LogFiles;
import main.java.message.CacheMessage;
import main.java.message.LogoutMessage;
import main.java.message.UpdateMessage;
import main.java.service.CustomService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;
import main.java.util.ShippingPackage;

public class ShippingService extends CustomService {

    private int cacheSize;
    private DatabaseConnector database;
    private Iterator<String> connectionIterator;
    private Lock connectionLock;
    private Lock shippingPackageQueueLock;
    private Map<String, TCPConnection> connections;
    private Queue<ShippingPackage> shippingPackageQueue;

    public ShippingService(ServerSecrets secrets) throws IOException {
        super(-1, LogFiles.SHIPPING_LOG);

        cacheSize = 3;
        database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
        connectionIterator = null;
        shippingPackageQueue = new LinkedList<ShippingPackage>();
        connections = new HashMap<String, TCPConnection>();
        connectionLock = new ReentrantLock();
        shippingPackageQueueLock = new ReentrantLock();
    }

    /**
     * Adds a new connection to the service.
     * 
     * @param nickname
     *            - hashed mail address of the user
     * @param newClient
     *            - socket of the connection
     * @return true in case of success<br>
     *         false in case of the user is already connected
     */
    public void addConnection(String nickname, TCPConnection newConnection) {
        connectionLock.lock();
        connections.put(nickname, newConnection);
        connectionLock.unlock();
    }

    public void addShippingPackage(String data, String resourceURI) {
        if (data != null) {
            try {
                ShippingPackage packet = new ShippingPackage(data, "POST", resourceURI);

                shippingPackageQueueLock.lock();
                shippingPackageQueue.add(packet);
                shippingPackageQueueLock.unlock();
            } catch (Exception e) {
            }
        }
    }

    public String getResource(String resourceURI) {
        String resourceData = null;
        CacheMessage cacheMessage = new CacheMessage();
        List<String> cache = database.readCache(resourceURI);

        if (cache.size() > 0) {
            int position = (int) (Math.random() * cache.size());

            connectionLock.lock();
            try {
                cacheMessage.setData(null);
                cacheMessage.setRequestMethod("GET");
                cacheMessage.setResource(resourceURI);
                while (!connections.get(cache.get(position)).isConnected()) {
                    position = (int) (Math.random() * cache.size());
                }
                connections.get(cache.get(position)).sendData(cacheMessage.getMessage());

                do {
                    try {
                        cacheMessage = CacheMessage.parse(connections.get(cache.get(position)).getData());
                    } catch (Exception e) {
                        break;
                    }
                } while (cacheMessage == null);

                resourceData = cacheMessage.getData();
            } catch (Exception e) {

            }
            connectionLock.unlock();
        }

        return resourceData;
    }

    public boolean sendUpdate(String userID, UpdateMessage message) {
        boolean wasSend = false;
        connectionLock.lock();

        TCPConnection connection = connections.get(userID);
        if (connection != null && connection.isConnected()) {
            try {
                connection.sendData(message.getMessage());
                wasSend = true;
            } catch (Exception e) {
            }
        }

        connectionLock.unlock();

        return wasSend;
    }

    @Override
    protected void service() {
        checkConnectedClients();
        checkShippingPackages();
    }

    private void checkConnectedClients() {
        connectionLock.lock();
        connectionIterator = connections.keySet().iterator();
        while (connectionIterator.hasNext()) {
            String key = connectionIterator.next();
            try {
                if (connections.get(key).hasData()) {
                    try {
                        String message = connections.get(key).getData();
                        while (message.equals("1") && connections.get(key).hasData()) {
                            message = connections.get(key).getData();
                        }

                        if (!message.equals("1")) {
                            JSONTokener parser = new JSONTokener(message);
                            JSONObject jsonMessage = (JSONObject) parser.nextValue();

                            switch (jsonMessage.getString("type")) {
                                case LogoutMessage.ID:
                                    deleteConnection(key, connectionIterator);
                                    break;
                                default:
                                    break;
                            }
                        }
                    } catch (ClassCastException cce) {
                        logger.writeLog("cannot parse incoming message to JSON", cce);
                    } catch (ClassNotFoundException cnfe) {
                        logger.writeLog("cannot parse incoming data to string datatype", cnfe);
                    }
                } else {
                    if (!connections.get(key).isConnected()) {
                        deleteConnection(key, connectionIterator);
                    }
                }
            } catch (IOException ioe) {
                deleteConnection(key, connectionIterator);
                ioe.printStackTrace();
                logger.writeLog("cannot read from inputstream", ioe);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                logger.writeLog("databse access error or closed result set", sqle);
            } catch (Exception e) {
                e.printStackTrace();
                logger.writeLog("unexpected exception" + System.lineSeparator(), e);
            }
        }
        connectionLock.unlock();
    }

    private void checkShippingPackages() {
        ShippingPackage packet = null;

        shippingPackageQueueLock.lock();
        if (!shippingPackageQueue.isEmpty()) {
            packet = shippingPackageQueue.poll();
        }
        shippingPackageQueueLock.unlock();

        connectionLock.lock();
        if (packet != null) {
            List<String> store = database.readCache(packet.getResource());
            CacheMessage cacheMessage = new CacheMessage();

            cacheMessage.setData(packet.getData());
            cacheMessage.setRequestMethod("POST");
            cacheMessage.setResource(packet.getResource());

            if (store.size() == 0) {
                distributePackage(cacheSize, cacheMessage, null);
            } else {
                Iterator<String> iterator = store.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    try {
                        if (connections.get(key).isConnected()) {
                            connections.get(key).sendData(cacheMessage.getMessage());
                        } else {
                            distributePackage(1, cacheMessage, store);
                        }
                    } catch (Exception e) {
                        iterator.remove();
                        distributePackage(1, cacheMessage, store);
                    }
                }
            }
        }
        connectionLock.unlock();
    }

    /**
     * Closes and deletes the connection which is identified by the key.
     * 
     * @param key
     *            - hashed mail of the user
     * @param iterator
     *            - iterator over the connection list
     * @throws IOException
     *             - if an I/O error occurs
     */
    private void deleteConnection(String key, Iterator<String> iterator) {
        try {
            connections.get(key).close();
            iterator.remove();
        } catch (IOException ioe) {
            logger.writeLog("cannot delete connection", ioe);
        }
    }

    private void distributePackage(int numberOfStores, CacheMessage message, List<String> usedKeys) {
        int temporaryStorage = 0;
        String key = "";
        List<String> alreadyUsedKeys = null;
        if (usedKeys == null) {
            alreadyUsedKeys = new ArrayList<String>(numberOfStores);
        } else {
            alreadyUsedKeys = usedKeys;
        }

        for (int i = 0; i < numberOfStores; i++) {
            temporaryStorage = (int) (Math.random() * connections.size());
            key = (String) connections.keySet().toArray()[temporaryStorage];
            while (!connections.get(key).isConnected() || alreadyUsedKeys.contains(key)) {
                temporaryStorage = (int) (Math.random() * connections.size());
                key = (String) connections.keySet().toArray()[temporaryStorage];
            }

            try {
                connections.get(key).sendData(message.getMessage());
                database.insertCache(message.getResource(), key);
            } catch (Exception e) {
                i--;
            }

            alreadyUsedKeys.add(i, key);
        }
    }
}
