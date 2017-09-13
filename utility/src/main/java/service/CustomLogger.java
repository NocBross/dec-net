package main.java.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomLogger {

	private File logFile;
	private PrintWriter writer;

	public CustomLogger(String absolutePathToLogFile) throws IOException {
		logFile = new File(absolutePathToLogFile);
		if (!logFile.exists()) {
			logFile.createNewFile();
		}

		writer = new PrintWriter(new FileOutputStream(logFile, true));
	}

	public void close() throws IOException {
		writer.close();
	}

	public synchronized void writeLog(String logMessage, Exception e) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		writer.write("[" + dateFormat.format(new Date()) + "] " + logMessage + System.lineSeparator());
		if (e != null) {
			e.printStackTrace(writer);
		}
		writer.flush();
	}

}
