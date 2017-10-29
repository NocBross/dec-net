package main.java.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.java.constants.ServerStatusCodes;
import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class RegisterHandler implements HttpHandler {

    private DatabaseConnector database;

    public RegisterHandler(ServerSecrets secrets) {
        database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String rawMessage = readMessage(httpExchange.getRequestBody());
        RegisterMessage message = RegisterMessage.parse(rawMessage);

        String userID = message.getUserID();
        ReportMessage report = new ReportMessage();
        report.setReferencedMessage(message.getType());
        if (database.insertNewUser(userID, message.getPassword())) {
            report.setResult(true);
            report.setStatusCode(ServerStatusCodes.REGISTER_CORRECT);
        } else {
            report.setResult(false);
            report.setStatusCode(ServerStatusCodes.REGISTER_KNOWN_USER_ID);
        }

        String rawReport = report.getMessage();
        httpExchange.getResponseHeaders().add("Content-type", "text/html");
        httpExchange.sendResponseHeaders(200, rawReport.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(rawReport.getBytes());
        os.close();
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
