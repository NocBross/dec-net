package main.java.connection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Wrapper class for a tcp connection.
 * 
 * @author Kay Oliver Szesny
 *
 */

public class TCPConnection {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private BufferedInputStream inBuff;

    public TCPConnection(Socket newSocket) throws Exception {
        socket = newSocket;
        socket.setSoTimeout(30000);

        inBuff = new BufferedInputStream(socket.getInputStream());
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
    }

    public TCPConnection(InetAddress serverAddress, int port) throws Exception {
        socket = new Socket(serverAddress, port);
        socket.setSoTimeout(30000);

        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        inBuff = new BufferedInputStream(socket.getInputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Closes the tcp connection.
     * 
     * @throws IOException
     *             - if an I/O error occurs.
     */
    public void close() throws IOException {
        try {
            out.close();
        } catch (Exception e) {
        }

        inBuff.close();
        in.close();
        socket.close();
    }

    /**
     * Returns the first received string.
     * 
     * @return received data
     * @throws Exception
     *             - Any of the usual Input/Output related exceptions or something
     *             is wrong with the decryption.
     */
    public String getData() throws Exception {
        return (String) in.readObject();
    }

    /**
     * Returns the ip address of the connection as a string.
     * 
     * @return current ip address
     */
    public String getInetAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    /**
     * Returns the local ip address of the connection as a string.
     * 
     * @return current ip address
     */
    public String getLocalAddress() {
        return socket.getLocalAddress().getHostAddress();
    }

    /**
     * Returns the port which is bind to this connection on the remote side.
     * 
     * @return remote port
     */
    public int getRemotePort() {
        return socket.getPort();
    }

    /**
     * Returns the local port which is bind to this connection.
     * 
     * @return client port
     */
    public int getLocalPort() {
        return socket.getLocalPort();
    }

    /**
     * Checks if the input stream hold some data.
     * 
     * @return true in case of the stream has some data received<br>
     *         false if no data are available
     * @throws IOException
     *             - Any of the usual Input/Output related exceptions.
     */
    public boolean hasData() throws IOException {
        if (inBuff.available() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests if the socket is still connected.
     * 
     * @return true in case of connected<br>
     *         false otherwise
     */
    public boolean isConnected() {
        // clear stream
        testConnection();
        testConnection();

        return testConnection();
    }

    /**
     * Sends the given data by using the output stream.
     * 
     * @param message
     *            - message which has to send
     * @throws Exception
     *             - Any exception thrown by the underlying OutputStream or if an
     *             I/O error has occurred or something is wrong with the encryption.
     */
    public void sendData(String message) throws Exception {
        out.writeObject(message);
        out.flush();
    }

    /**
     * Tests if the socket is still connected to the server by sending a test
     * string. This method is called from the isConnected method.
     * 
     * @return true in case of connected<br>
     *         false in case of exception
     */
    private boolean testConnection() {
        try {
            out.writeObject("1");
            out.flush();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }
}
