package main.java.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.PrivateKey;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class CustomService extends Thread {

	protected Lock controlLock;
	protected ServerSocket socket;
	protected CustomLogger logger;
	protected boolean run;
	protected final int KILL_TIME_IN_SECONDS;
	protected PrivateKey key;

	public CustomService(int port, PrivateKey key, String absolutePathToLogFile) throws IOException {
		KILL_TIME_IN_SECONDS = 10;

		if (port != -1) {
			socket = new ServerSocket(port);
			socket.setSoTimeout(KILL_TIME_IN_SECONDS * 1000);
		} else {
			socket = null;
		}

		logger = new CustomLogger(absolutePathToLogFile);
		this.key = key;

		controlLock = new ReentrantLock();
		run = true;
	}

	@Override
	public void run() {
		controlLock.lock();

		while (run) {
			controlLock.unlock();
			service();
			controlLock.lock();
		}

		controlLock.unlock();
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException ioe) {
			logger.writeLog("error by closing the socket", ioe);
		}
	}

	/**
	 * Stops the thread after all running actions are done.
	 */
	public void stopService() {
		controlLock.lock();
		run = false;
		controlLock.unlock();
	}

	/**
	 * Executes the specific service actions.
	 */
	protected abstract void service();

}
