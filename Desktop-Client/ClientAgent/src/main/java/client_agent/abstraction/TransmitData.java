package main.java.client_agent.abstraction;

public class TransmitData {

	private String destination;
	private String message;

	public TransmitData(String destination, String message) {
		this.destination = destination;
		this.message = message;
	}

	/**
	 * Returns the destination of the message.
	 * 
	 * @return name of the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Returns the message which has to send.
	 * 
	 * @return message to send
	 */
	public String getMessage() {
		return message;
	}
}
