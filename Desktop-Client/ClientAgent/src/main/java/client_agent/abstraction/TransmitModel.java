package main.java.client_agent.abstraction;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransmitModel {

	private Lock messageQueueLock;
	private Queue<TransmitData> messageQueue;

	public TransmitModel() {
		messageQueueLock = new ReentrantLock();
		messageQueue = new LinkedList<TransmitData>();
	}

	/**
	 * Adds a new message to the message queue. The messages which are added here
	 * will be split up by the split controller.
	 * 
	 * @param message
	 *            - new message which has to send
	 */
	public void addMessage(TransmitData message) {
		messageQueueLock.lock();
		messageQueue.add(message);
		messageQueueLock.unlock();
	}

	/**
	 * Returns the message which has to split up next.
	 * 
	 * @return message to split up
	 */
	public TransmitData getMessage() {
		TransmitData message = null;
		messageQueueLock.lock();

		message = messageQueue.poll();

		messageQueueLock.unlock();
		return message;
	}

	/**
	 * Returns true if the message queue contains no messages.
	 * 
	 * @return true if no messages are left
	 */
	public boolean isMessageQueueEmpty() {
		boolean state = false;
		messageQueueLock.lock();

		state = messageQueue.isEmpty();

		messageQueueLock.unlock();
		return state;
	}
}
