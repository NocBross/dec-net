package main.java.client_agent;

import java.net.UnknownHostException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import main.java.client_agent.controller.ResourceController;
import main.java.client_agent.controller.TransmitController;
import main.java.constants.AgentID;
import main.java.constants.Network;
import main.java.constants.WebServiceConstants;
import main.java.message.CacheMessage;
import main.java.message.LoginMessage;
import main.java.message.LogoutMessage;
import main.java.message.Message;
import main.java.message.RDFMessage;
import main.java.message.ReportMessage;

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
    private ResourceController resourceController;
    private TransmitController transmitController;

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
                if (newValue != null) {
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
        try {
            primaryStage.hide();

            LogoutMessage message = new LogoutMessage();
            message.setSender(userModel.getUserID());
            sendMessage(Network.NETWORK_HUB, message);
            scatterAllMessage(message.getMessage());
            transmitController.stopController();
            receiveController.stopController();

            transmitController.join();
            receiveController.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    @Override
    public Parent getScene() {
        return activeAgent.getScene();
    }

    @Override
    public void receiveMessage(String message) {
        handleCacheMessage(message);
        handleReceiveRDF(message);
        handleReportMessage(message);
        scatterMessage(message);

        newMessage.set(null);
    }

    @Override
    public void scatterMessage(String message) {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).receiveMessage(message);
        }
    }

    @Override
    public void sendMessage(String url, Message message) {
        boolean messageHasToSend = true;

        messageHasToSend = handleLoginMessage(message);
        messageHasToSend = handleSendRDF(url, message);

        if (messageHasToSend) {
            if (message != null) {
                transmitController.addMessage(url, message.getMessage());
            } else {
                transmitController.addMessage(url, null);
            }
        }
    }

    @Override
    public void switchAgent(AgentID destinationAgent) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < children.size(); i++) {
                    if (children.get(i).getID() == destinationAgent) {
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
     *            - new received message
     */
    public void updateMessage(String newMessage) {
        messageLock.lock();
        this.newMessage.setValue(newMessage);
        messageLock.unlock();
    }

    @Override
    public void storeRDFModel(RDFMessage message) {
        resourceController.updateResource(message);
    }

    /**
     * Creates instances of all necessary controllers for the client agent.
     * 
     * @throws Exception
     */
    private void createController() throws Exception {
        receiveController = new ReceiveController(connectionModel, this);
        resourceController = new ResourceController("context");
        transmitController = new TransmitController(this, connectionModel, transmitModel);
    }

    /**
     * Creates instances of all necessary models for the client agent.
     * 
     * @throws UnknownHostException
     */
    private void createModels() throws Exception {
        connectionModel = new ConnectionModel();
        userModel = new UserModel();
        transmitModel = new TransmitModel();
    }

    private void handleCacheMessage(String message) {
        CacheMessage cacheMessage = CacheMessage.parse(message);
        if (cacheMessage != null) {
            if (cacheMessage.getRequestMethod().equals("GET")) {
                RDFMessage rdfMessage = resourceController.getResource(cacheMessage.getResource());
                cacheMessage.setData(rdfMessage.getMessage());
                transmitController.addMessage(Network.NETWORK_HUB, cacheMessage.getMessage());
            } else {
                resourceController.updateResource(RDFMessage.parse(cacheMessage.getData()));
            }
        }
    }

    /**
     * Stored the mail address and password from an outgoing login message in the
     * local model.
     * 
     * @param message
     *            - login message
     */
    private boolean handleLoginMessage(Message message) {
        if (message != null) {
            LoginMessage loginMessage = LoginMessage.parse(message.getMessage());
            if (loginMessage != null) {
                userModel.setUserID(loginMessage.getNickname());
                userModel.setPassword(loginMessage.getPassword());

                String url = "/" + userModel.getResourceHubAddress() + "/" + userModel.getNickname()
                        + WebServiceConstants.PROFILE_RESOURCE;
                RDFMessage rdfMessage = resourceController.getResource(url);
                if (rdfMessage != null) {
                    updateMessage(rdfMessage.getMessage());
                } else {
                    url = Network.NETWORK_PROTOCOL + userModel.getResourceHubAddress() + ":"
                            + Network.SERVER_WEBSERVICE_PORT + "/" + userModel.getNickname()
                            + WebServiceConstants.PROFILE_RESOURCE;
                    transmitController.addMessage(url, null);
                }
            }
        }

        return true;
    }

    /**
     * Handles an incoming rdf message if the given message is a serialized version
     * of a rdf message.<br>
     * This method will store the model which is send by this message locally.
     * 
     * @param message
     *            - incoming message
     */
    private void handleReceiveRDF(String message) {
        RDFMessage rdfMessage = RDFMessage.parse(message);
        if (rdfMessage != null) {
            resourceController.updateResource(rdfMessage);
        }
    }

    /**
     * Handles an incoming report message.
     * 
     * @param message
     *            - report message
     */
    private void handleReportMessage(String message) {
        ReportMessage reportMessage = ReportMessage.parse(message);
        if (reportMessage != null) {
            if (reportMessage.getReferencedMessage().equals(LoginMessage.ID) && !reportMessage.getResult()) {
                connectionModel.deleteServerConnection();
            }
        }
    }

    /**
     * Checks if the requested resource in the RDFMessage is stored locally.
     * 
     * @param message
     *            - outgoing rdf message
     * @return returns true if the resource is not stored locally and the request
     *         has to send to the server, false otherwise
     */
    private boolean handleSendRDF(String url, Message message) {
        if (message != null) {
            RDFMessage rdfMessage = RDFMessage.parse(message.getMessage());
            if (rdfMessage != null) {
                RDFMessage storedModel = resourceController.getResource(rdfMessage.getResourceID());
                if (storedModel != null) {
                    updateMessage(storedModel.getMessage());
                    return false;
                }
            }
        } else {
            RDFMessage storedModel = resourceController.getResource(
                    url.replace(Network.NETWORK_PROTOCOL, "").replace(":" + Network.SERVER_WEBSERVICE_PORT, ""));
            if (storedModel != null) {
                updateMessage(storedModel.getMessage());
                return false;
            }
        }

        return true;
    }

    /**
     * Starts all necessary controllers.
     */
    private void startController() {
        receiveController.start();
        transmitController.start();
    }
}
