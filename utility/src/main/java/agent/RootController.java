package main.java.agent;

public interface RootController {

	/**
	 * Interprets the incoming message as the result of the send request.<br>
	 * If the message is not the answer of the request the message will be ignored.
	 * 
	 * @param message
	 *            - incoming message
	 */
	public void receiveResult(String message);

	/**
	 * Sets a new agent to the controller.
	 * 
	 * @param newAgent
	 *            - new agent of the controller
	 */
	public void setAgent(CustomAgent newAgent);
}
