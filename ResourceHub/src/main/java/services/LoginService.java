package main.java.services;

import java.io.IOException;
import java.net.SocketTimeoutException;

import main.java.connection.TCPConnection;
import main.java.constants.LogFiles;
import main.java.message.LoginMessage;
import main.java.message.ReportMessage;
import main.java.service.CustomService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class LoginService extends CustomService {

	private DatabaseConnector database;

	public LoginService(int port, ServerSecrets secrets) throws IOException {
		super(port, LogFiles.LOGIN_LOG);
		database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
	}

	@Override
	protected void service() {
		TCPConnection clientConnection = null;
		LoginMessage message = null;

		try {
			clientConnection = new TCPConnection(socket.accept());

			do {
				message = LoginMessage.parse(clientConnection.getData());
			} while (message == null);

			String nickname = message.getNickname();
			String password = message.getPassword();
			ReportMessage report = new ReportMessage();
			report.setReferencedMessage(message.getType());
			if (database.loginQuery(nickname, password)) {
				report.setResult(true);
			} else {
				report.setResult(false);
				report.setErrorCode("Benutzername oder Passwort falsch!");
			}

			clientConnection.sendData(report.getMessage());
			clientConnection.close();
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

}
