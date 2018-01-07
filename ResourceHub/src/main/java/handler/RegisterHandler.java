package main.java.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.java.constants.LogFiles;
import main.java.constants.ServerStatusCodes;
import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;
import main.java.service.CustomLogger;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class RegisterHandler implements HttpHandler {

    private CustomLogger logger;
    private DatabaseConnector database;
    private String logID;

    public RegisterHandler(ServerSecrets secrets) throws Exception {
        logger = new CustomLogger(LogFiles.REGISTER_LOG);
        database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
        logID = "RegisterService";
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.writeLog(logID + " new register request received", null);
        String rawMessage = readMessage(httpExchange.getRequestBody());
        RegisterMessage message = RegisterMessage.parse(rawMessage);
        ReportMessage report = new ReportMessage();
        report.setReferencedMessage(RegisterMessage.ID);

        if (message != null) {
            logger.writeLog(
                    logID + " correct message type was send from " + httpExchange.getRemoteAddress().getHostName(),
                    null);
            String userID = message.getUserID();
            if (database.insertNewUser(userID, message.getPassword())) {
                logger.writeLog(
                        logID + " registration was successful from " + httpExchange.getRemoteAddress().getHostName(),
                        null);
                report.setResult(true);
                report.setStatusCode(ServerStatusCodes.REGISTER_CORRECT);
            } else {
                logger.writeLog(logID + " registration was not successful from "
                        + httpExchange.getRemoteAddress().getHostName(), null);
                report.setResult(false);
                report.setStatusCode(ServerStatusCodes.REGISTER_KNOWN_USER_ID);
            }
        } else {
            logger.writeLog(
                    logID + " incorrect message type was send from " + httpExchange.getRemoteAddress().getHostName(),
                    null);
            report.setResult(false);
            report.setStatusCode(ServerStatusCodes.WRONG_MESSAGE_TYPE);
        }

        logger.writeLog(logID + " sending report to " + httpExchange.getRemoteAddress().getHostName(), null);
        String rawReport = report.getMessage();
        httpExchange.getResponseHeaders().add("Content-type", "text/html");
        httpExchange.sendResponseHeaders(200, rawReport.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(rawReport.getBytes());
        os.close();
        httpExchange.close();
    }

    private String readMessage(InputStream in) throws IOException {
        String message = "";
        String line = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        while ((line = reader.readLine()) != null) {
            message += line;
        }

        reader.close();
        return message;
    }

}
