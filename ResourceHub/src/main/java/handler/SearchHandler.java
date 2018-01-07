package main.java.handler;

import java.io.OutputStream;
import java.sql.ResultSet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.java.constants.LogFiles;
import main.java.constants.WebServiceConstants;
import main.java.message.SearchMessage;
import main.java.service.CustomLogger;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class SearchHandler implements HttpHandler {

    private CustomLogger logger;
    private DatabaseConnector database;
    private String logID;

    public SearchHandler(ServerSecrets secrets) throws Exception {
        logger = new CustomLogger(LogFiles.SEARCH_LOG);
        database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
        logID = "SearchService";
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        OutputStream os = null;
        try {
            logger.writeLog(logID + " new search request received", null);
            int status = 400;
            String response = "<b>BAD REQUEST</b>";
            String request = httpExchange.getRequestURI().toString();
            String[] searchRequests = request.split(WebServiceConstants.CONTEXT_SEPARATOR_ESCAPED);

            if (searchRequests.length == 2) {
                logger.writeLog(
                        logID + " correct format was send from " + httpExchange.getRemoteAddress().getHostName(), null);
                String[] searchRequest = searchRequests[1].split(WebServiceConstants.KEY_VALUE_SEPARATOR);

                switch (searchRequest[0]) {
                    case WebServiceConstants.USER_ID_KEY:
                        logger.writeLog(logID + " looking for nickname " + searchRequest[1], null);
                        ResultSet result = database.lookingForNickname(searchRequest[1]);
                        SearchMessage resultMessage = new SearchMessage();
                        logger.writeLog(logID + " reading results", null);
                        while (result.next()) {
                            resultMessage.addNickname(result.getString(1));
                        }
                        response = resultMessage.getMessage();
                        status = 200;
                        break;
                    default:
                        response = "<b>NOT FOUND</b>";
                        status = 404;
                        break;
                }
            }

            logger.writeLog(logID + " sending response to " + httpExchange.getRemoteAddress().getHostName(), null);
            httpExchange.getResponseHeaders().add("Content-type", "text/html");
            httpExchange.sendResponseHeaders(status, response.length());
            os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.flush();
        } catch (Exception e) {
            logger.writeLog(logID + " error while handle search request", e);
        }

        if (os != null) {
            try {
                os.close();
            } catch (Exception e) {
                logger.writeLog(logID + " error while closing output stream", e);
            }
        }
        httpExchange.close();
    }
}
