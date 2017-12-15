package main.java.launcher;

import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import main.java.client_agent.controller.ResourceController;
import main.java.connection.TCPConnection;
import main.java.message.CacheMessage;
import main.java.message.LoginMessage;
import main.java.message.RDFMessage;
import main.java.message.ReportMessage;

public class DummyThread extends Thread {

    private boolean isRunning;
    private String nickname;
    private Lock runLock;
    private ResourceController resourceController;
    private TCPConnection connection;

    public DummyThread(InetAddress serverAddress, int port, String userID, String password,
            ResourceController resourceController) throws Exception {
        isRunning = false;
        this.nickname = userID;
        this.resourceController = resourceController;
        runLock = new ReentrantLock();

        login(serverAddress, port, userID, password);
    }

    @Override
    public void run() {
        String message = null;
        runLock.lock();

        System.out.println(nickname + " is running");

        while (isRunning) {
            runLock.unlock();
            try {
                message = null;
                do {
                    message = connection.getData();
                } while (message.equals("1"));

                handleCacheMessage(message);
            } catch (SocketTimeoutException ste) {

            } catch (Exception e) {
                e.printStackTrace();
            }
            runLock.lock();
        }

        runLock.unlock();
    }

    public void startClient() {
        runLock.lock();
        isRunning = true;
        super.start();
        runLock.unlock();
    }

    public void stopClient() {
        runLock.lock();
        isRunning = false;
        runLock.lock();
    }

    private void handleCacheMessage(String message) throws Exception {
        CacheMessage cacheMessage = CacheMessage.parse(message);
        if (cacheMessage != null) {
            System.out.println(message);
            if (cacheMessage.getRequestMethod().equals("GET")) {
                RDFMessage rdfMessage = resourceController.getResource(cacheMessage.getResource());
                cacheMessage.setData(rdfMessage.getMessage());
                connection.sendData(cacheMessage.getMessage());
            } else {
                resourceController.updateResource(RDFMessage.parse(cacheMessage.getData()));
            }
        }
    }

    private void login(InetAddress serverAddress, int port, String userID, String password) throws Exception {
        ReportMessage report = null;
        LoginMessage loginMessage = new LoginMessage();
        if (!loginMessage.setNickname(userID)) {
            throw new Exception("no valid userID");
        }
        if (!loginMessage.setPassword(password)) {
            throw new Exception("no valid password");
        }

        connection = new TCPConnection(serverAddress, port);
        connection.sendData(loginMessage.getMessage());

        do {
            report = ReportMessage.parse(connection.getData());
        } while (report == null);
        if (!report.getResult()) {
            throw new Exception("login error: wrong userID or password");
        }
    }
}
