package main.java.services;

import java.io.IOException;
import java.net.SocketTimeoutException;

import main.java.connection.TCPConnection;
import main.java.constants.LogFiles;
import main.java.message.RegisterMessage;
import main.java.message.ReportMessage;
import main.java.service.CustomService;
import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;

public class RegisterService extends CustomService {

	private DatabaseConnector database;

	public RegisterService(int port, ServerSecrets secrets) throws Exception {
		super(port, LogFiles.REGISTER_LOG);
		database = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());
	}

	@Override
	protected void service() {
		TCPConnection clientConnection = null;
		RegisterMessage message = null;

		try {
			clientConnection = new TCPConnection(socket.accept());

			do {
				message = RegisterMessage.parse(clientConnection.getData());
			} while (message == null);

			String nickname = message.getNickname();
			ReportMessage report = new ReportMessage();
			report.setReferencedMessage(message.getType());
			if (database.insertNewUser(nickname, message.getPassword())) {
				report.setResult(true);
			} else {
				report.setResult(false);
				report.setErrorCode("Benutzername bereits vorhanden");
			}

			clientConnection.sendData(report.getMessage());
			clientConnection.close();
		} catch (SocketTimeoutException ste) {
			if (clientConnection != null) {
				try {
					clientConnection.close();
				} catch (IOException ioe) {
					logger.writeLog("can not close the connection after SocketTimeoutException", ioe);
				}
			}
		} catch (InterruptedException ie) {
			logger.writeLog("the mail-thread was interrupted" + System.lineSeparator(), ie);
		} catch (Exception e) {
			logger.writeLog("something is wrong with the streams or the incoming message", e);
		}
	}
}
