package main.java.agent;

public interface RequestController {

	/**
	 * Interprets the incoming message as the result of the send request.<br>
	 * If the message is not the answer of the request the message will be ignored.
	 * 
	 * @param message
	 *            - incoming message
	 * @return error code for exception handling
	 */
	public int receiveResult(String message);

	/**
	 * Validates the stored data, generates the message and send it.
	 * 
	 * @return error code for exception handling
	 */
	public int sendRequest();

	/**
	 * Sets a new agent to the controller.
	 * 
	 * @param newAgent
	 *            - new agent of the controller
	 */
	public void setAgent(LTAgent newAgent);
}
