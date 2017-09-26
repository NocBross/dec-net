package main.java.watchdog;

import main.java.service.CustomService;

public class ServiceWatchdog extends Thread {

	private boolean stopThreads;
	private boolean isRunning;
	private int crashedThread;
	private final long WAIT_TIME;
	private CustomService[] services;

	public ServiceWatchdog(CustomService[] services) {
		stopThreads = false;
		isRunning = false;
		crashedThread = -1;
		WAIT_TIME = 30 * 1000;
		this.services = services;
	}

	@Override
	public void run() {
		isRunning = true;

		try {
			while (isRunning) {
				Thread.sleep(WAIT_TIME);
				System.gc();

				for (int i = 0; i < services.length; i++) {
					if (!services[i].isAlive()) {
						crashedThread = i;
						stopThreads = true;
						break;
					}
				}

				if (stopThreads) {
					for (int i = 0; i < services.length; i++) {
						if (i != crashedThread) {
							services[i].stopService();
							services[i].join();
						}
					}

					stopWatchdog();
				}
			}
		} catch (InterruptedException ie) {
			System.out.println("Thread was interrupted.\nWatchdog is shutting down.");
		}
	}

	/**
	 * Stops the watchdog.
	 */
	public void stopWatchdog() {
		isRunning = false;
	}

}
