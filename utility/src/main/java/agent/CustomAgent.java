package main.java.agent;

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;

import javafx.scene.Parent;
import main.java.constants.AgentID;

/**
 * The agent super class defines the structure of each agent of the PAC pattern.
 * 
 * @author developer
 *
 */

public abstract class CustomAgent {

    protected CustomAgent parent;
    protected List<CustomAgent> children;
    protected AgentID ID;

    public CustomAgent(CustomAgent parent, AgentID ID) {
        this.parent = parent;
        this.ID = ID;
        children = new LinkedList<CustomAgent>();
    }

    /**
     * Adds a new child agent to this one.
     * 
     * @param newChild
     *            - new agent which has to be add
     * @return true in case of success; false otherwise
     */
    public boolean addChild(CustomAgent newChild) {
        boolean existing = false;

        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getID() == newChild.getID()) {
                existing = true;
                break;
            }
        }

        if (!existing) {
            return children.add(newChild);
        } else {
            return false;
        }
    }

    /**
     * Deletes a child at the given index.
     * 
     * @param index
     *            - index of the agent which has to delete
     * @return true in case of success; false otherwise
     */
    public boolean deleteChild(int index) {
        try {
            children.remove(index);
            return true;
        } catch (IndexOutOfBoundsException ioobe) {
            return false;
        }
    }

    /**
     * Deletes a child with the given ID.
     * 
     * @param agentToDelete
     *            - ID of the agent which has to delete
     * @return true in case of success; false otherwise
     */
    public boolean deleteChild(AgentID agentToDelete) {
        int index = -1;

        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getID() == agentToDelete) {
                index = i;
                break;
            }
        }

        try {
            children.remove(index);
            return true;
        } catch (IndexOutOfBoundsException ioobe) {
            return false;
        }
    }

    /**
     * Returns the ID of the agent.
     * 
     * @return agent ID
     */
    public AgentID getID() {
        return ID;
    }

    /**
     * Returns the scene of the agent module.
     * 
     * @return root node of scene of the module
     */
    public abstract Parent getScene();

    /**
     * Receives data from the server or other clients.
     * 
     * @param message
     *            - message which was received
     */
    public abstract void receiveMessage(String message);

    /**
     * Sends data to the server or to other clients.
     * 
     * @param destination
     *            - connection end point which will get the message
     * @param message
     *            - massage which has to send to the end point
     */
    public abstract void sendMessage(String destination, String message);

    /**
     * Distributes the given message to all children.
     * 
     * @param message
     *            - message which has to send to another child of this middle agent
     */
    public abstract void scatterMessage(String message);

    /**
     * Distributes the given message to all agents.
     * 
     * @param message
     *            - message which has to send to all agents
     */
    public void scatterAllMessage(String message) {
        if (parent == null) {
            receiveMessage(message);
        } else {
            parent.scatterAllMessage(message);
        }
    }

    /**
     * Stores the given model.
     * 
     * @param rdfModel
     */
    public abstract void storeRDFModel(String resourcePath, Model rdfModel);

    /**
     * Hides the current agent and shows the given destination agent.
     * 
     * @param destinationAgent
     *            - new agent which has to show
     */
    public abstract void switchAgent(AgentID destinationAgent);

}
