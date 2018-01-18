package main.java.launcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

import main.java.client_agent.controller.ResourceController;
import main.java.constants.Network;
import main.java.service.CustomLogger;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class DummyClientLauncher extends Thread {

    public static void main(String[] args) throws Exception {
        final int numberOfClients = 3;
        DummyThread[] dummyClients = new DummyThread[numberOfClients];
        ServerSecrets secrets = new ServerSecrets();
        secrets.loadServerSecrets();
        DatabaseConnector database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());

        for(int i = 0; i < numberOfClients; i++ ) {
            String nickname = "dummy" + (i + 1);
            String password = "123aBc_4!u";

            database.insertNewUser(nickname, password);
            dummyClients[i] = new DummyThread(InetAddress.getByName("localhost"), Network.NETWORK_HUB_PORT, nickname,
                    password, new ResourceController("data/", new CustomLogger("logs/dummy-" + (i + 1) + ".txt")));
            dummyClients[i].startClient();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();

        for(int i = 0; i < numberOfClients; i++ ) {
            dummyClients[i].stopClient();
            dummyClients[i].join();
        }
    }

}
