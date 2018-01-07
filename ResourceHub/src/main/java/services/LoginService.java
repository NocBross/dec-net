package main.java.services;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import main.java.connection.TCPConnection;
import main.java.constants.LogFiles;
import main.java.constants.ServerStatusCodes;
import main.java.message.LoginMessage;
import main.java.message.ReportMessage;
import main.java.message.UpdateMessage;
import main.java.service.CustomService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class LoginService extends CustomService {

    private DatabaseConnector database;
    private ShippingService shippingService;

    public LoginService(int port, ServerSecrets secrets, ShippingService shippingService) throws IOException {
        super(port, LogFiles.LOGIN_LOG, "LoginService");
        database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
        this.shippingService = shippingService;
    }

    @Override
    protected void service() {
        TCPConnection clientConnection = null;
        LoginMessage message = null;

        try {
            clientConnection = new TCPConnection(socket.accept());
            logger.writeLog(logID + " new connection accepted with " + clientConnection.getInetAddress(), null);

            do {
                message = LoginMessage.parse(clientConnection.getData());
            } while (message == null);

            String userID = message.getNickname();
            String password = message.getPassword();
            ReportMessage report = new ReportMessage();
            report.setReferencedMessage(message.getType());
            if (database.loginQuery(userID, password)) {
                logger.writeLog(logID + " correct login data from " + clientConnection.getInetAddress(), null);
                report.setResult(true);
                report.setStatusCode(ServerStatusCodes.LOGIN_CORRECT);
            } else {
                logger.writeLog(logID + " incorrect login data from " + clientConnection.getInetAddress(), null);
                report.setResult(false);
                report.setStatusCode(ServerStatusCodes.LOGIN_UNKNOWN_USER_ID);
            }

            logger.writeLog(logID + " sending report to " + clientConnection.getInetAddress(), null);
            clientConnection.sendData(report.getMessage());
            if (report.getResult()) {
                logger.writeLog(logID + " transfer connection to AhippingService", null);
                shippingService.addConnection(userID, clientConnection);
                logger.writeLog(logID + " sending UpdateMessages to " + clientConnection.getInetAddress(), null);
                sendUpdateMessages(userID);
            } else {
                logger.writeLog(logID + " close connection to " + clientConnection.getInetAddress(), null);
                clientConnection.close();
            }
        } catch (SocketTimeoutException ste) {
            if (clientConnection != null) {
                try {
                    clientConnection.close();
                } catch (IOException ioe) {
                    logger.writeLog("can not close the connection after SocketTimeout", ioe);
                }
            }
        } catch (Exception e) {
            logger.writeLog("something is wrong with the streams or the incoming message", e);
        }
    }

    private void sendUpdateMessages(String userID) {
        List<String> messages = database.readUpdateMessages(userID);

        for (int i = 0; i < messages.size(); i++) {
            UpdateMessage message = UpdateMessage.parse(messages.get(i));
            if (shippingService.sendUpdate(userID, message)) {
                database.deleteUpdateMessage(userID, messages.get(i));
            }
        }
    }

}
