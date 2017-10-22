package main.java.client_agent;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.jena.rdf.model.Model;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.stage.Stage;
import main.java.AuthenticationAgent;
import main.java.SocNetAgent;
import main.java.agent.CustomAgent;
import main.java.client_agent.abstraction.ConnectionModel;
import main.java.client_agent.abstraction.TransmitModel;
import main.java.client_agent.abstraction.UserModel;
import main.java.client_agent.controller.ReceiveController;
import main.java.client_agent.controller.TransmitController;
import main.java.constants.AgentID;
import main.java.constants.Network;
import main.java.message.LoginMessage;
import main.java.message.RDFMessage;
import main.java.message.ReportMessage;
import main.java.webserver.ClientWebServer;

/**
 * The ClientAgent is the root of the agent tree structure.<br>
 * He defines the way to access the database and stores all connections.
 * 
 * @author developer
 *
 */

public class ClientAgent extends CustomAgent {

    private Stage primaryStage;

    private Lock messageLock;
    private CustomAgent activeAgent;
    private StringProperty newMessage;

    private ConnectionModel connectionModel;
    private UserModel userModel;
    private TransmitModel transmitModel;

    private ReceiveController receiveController;
    private TransmitController transmitController;

    private ClientWebServer webserver;


    public ClientAgent(Stage primaryStage) throws Exception {
        super(null, AgentID.CLIENT_AGENT);
        this.primaryStage = primaryStage;

        addChild(new AuthenticationAgent(this, primaryStage));
        addChild(new SocNetAgent(this, primaryStage));

        messageLock = new ReentrantLock();
        activeAgent = children.get(0);
        newMessage = new SimpleStringProperty();
        newMessage.addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                messageLock.lock();
                if(newValue != null) {
                    receiveMessage(newValue);
                }
                messageLock.unlock();
            }

        });

        createModels();
        createController();
        startController();
    }


    public void close() {
        System.exit(0);
    }


    @Override
    public Parent getScene() {
        return activeAgent.getScene();
    }


    @Override
    public void receiveMessage(String message) {
        handleReportMessage(message);
        scatterMessage(message);

        newMessage.set(null);
    }


    @Override
    public void scatterMessage(String message) {
        for(int i = 0; i < children.size(); i++ ) {
            children.get(i).receiveMessage(message);
        }
    }


    @Override
    public void sendMessage(String destination, String message) {
        transmitController.addMessage(destination, message);
        handleLoginMessage(message);
    }


    @Override
    public void switchAgent(AgentID destinationAgent) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                for(int i = 0; i < children.size(); i++ ) {
                    if(children.get(i).getID() == destinationAgent) {
                        primaryStage.getScene().setRoot(children.get(i).getScene());
                        activeAgent = children.get(i);
                        break;
                    }
                }
            }

        });
    }


    /**
     * Changes the message in the string property.
     * 
     * @param newMessage
     *        - new received message
     */
    public void updateMessage(String newMessage) {
        messageLock.lock();
        this.newMessage.setValue(newMessage);
        messageLock.unlock();
    }


    @Override
    public void storeRDFModel(String resourcePath, Model rdfModel) {
        try {
            ByteArrayOutputStream stringWriter = new ByteArrayOutputStream();
            rdfModel.write(stringWriter);
            RDFMessage rdfMessage = new RDFMessage(userModel.getNickname(), stringWriter.toString());
            String url = Network.LOCALHOST_URL + resourcePath;
            transmitController.addMessage(url, rdfMessage.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates instances of all necessary controllers for the client agent.
     * 
     * @throws Exception
     */
    private void createController() throws Exception {
        receiveController = new ReceiveController(connectionModel, this);
        transmitController = new TransmitController(connectionModel, transmitModel);
        webserver = new ClientWebServer(Network.CLIENT_WEBSERVER_PORT);
    }


    /**
     * Creates instances of all necessary models for the client agent.
     * 
     * @throws UnknownHostException
     */
    private void createModels() throws Exception {
        connectionModel = new ConnectionModel(InetAddress.getByName("localhost"));
        userModel = new UserModel();
        transmitModel = new TransmitModel();
    }


    /**
     * Stored the mail address and password from an outgoing login message in
     * the local model.
     * 
     * @param message
     *        - login message
     */
    private void handleLoginMessage(String message) {
        LoginMessage loginMessage = LoginMessage.parse(message);
        if(loginMessage != null) {
            userModel.setNickname(loginMessage.getNickname());
            userModel.setPassword(loginMessage.getPassword());

            String url = "http://localhost:" + Network.CLIENT_WEBSERVER_PORT + "/" + loginMessage.getNickname()
                    + "/profile";
            transmitController.addMessage(url, null);
        }
    }


    /**
     * Handles an incoming report message.
     * 
     * @param message
     *        - report message
     */
    private void handleReportMessage(String message) {
        ReportMessage reportMessage = ReportMessage.parse(message);
        if(reportMessage != null) {
            if(reportMessage.getReferencedMessage().equals(LoginMessage.ID) && reportMessage.getResult()) {} else {
                receiveController.stopController();

                connectionModel.lockServerConnection();
                connectionModel.deleteServerConnection();
                receiveController = new ReceiveController(connectionModel, this);
                receiveController.start();
                connectionModel.unlockServerConnection();
            }
        }
    }


    /**
     * Starts all necessary controllers.
     */
    private void startController() {
        receiveController.start();
        transmitController.start();
        webserver.start();
    }
}
