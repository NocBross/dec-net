package main.java.message;

import org.json.JSONObject;
import org.json.JSONTokener;

public class RDFMessage extends LTMessage {

    public static final String ID = "rdf";
    private static final String RESOURCE_ID = "resourceID";
    private static final String RDF_MODEL = "model";


    public RDFMessage(String resourceID, String model) {
        super(ID);
        messageData.put(RESOURCE_ID, resourceID);
        messageData.put(RDF_MODEL, model);
    }


    /**
     * Creates a RDFMessage from a given string.
     * 
     * @param messageString
     *        - string which will be converted
     * @return RDFMessage of the string or null if the string not representing a
     *         RDFMessage
     */
    public static RDFMessage parse(String messageString) {
        if(messageString != null) {
            try {
                JSONTokener parser = new JSONTokener(messageString);
                JSONObject jsonMessage = (JSONObject) parser.nextValue();

                if(jsonMessage.getString(TYPE).equals(ID)) {
                    String resourceID = jsonMessage.getString(RESOURCE_ID);
                    String model = jsonMessage.getString(RDF_MODEL);
                    RDFMessage message = new RDFMessage(resourceID, model);

                    return message;
                } else {
                    return null;
                }
            } catch(Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }


    public String getResourceID() {
        return messageData.get(RESOURCE_ID);
    }


    public String getModel() {
        return messageData.get(RDF_MODEL);
    }

}
