package main.java.handler;

import java.io.OutputStream;
import java.sql.ResultSet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import main.java.constants.WebServiceConstants;
import main.java.message.SearchMessage;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class SearchHandler implements HttpHandler {

	private DatabaseConnector database;

	public SearchHandler(ServerSecrets secrets) {
		database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
	}

	@Override
	public void handle(HttpExchange httpExchange) {
		try {
			int status = 400;
			String response = "";
			String request = httpExchange.getRequestURI().toString();
			String[] searchRequests = request.split(WebServiceConstants.CONTEXT_SEPARATOR_ESCAPED);

			httpExchange.getResponseHeaders().add("Content-type", "text/html");
			for (int i = 1; i < searchRequests.length; i++) {
				String[] searchRequest = searchRequests[i].split(WebServiceConstants.KEY_VALUE_SEPARATOR);

				switch (searchRequest[0]) {
				case WebServiceConstants.USER_ID_KEY:
					ResultSet result = database.lookingForNickname(searchRequest[1]);
					SearchMessage resultMessage = new SearchMessage();
					while (result.next()) {
						resultMessage.addNickname(result.getString(1));
					}
					response = resultMessage.getMessage();
					status = 200;
					break;
				default:
					response = "";
					status = 404;
					i = searchRequests.length;
					break;
				}
			}

			httpExchange.sendResponseHeaders(status, response.length());
			OutputStream os = httpExchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
