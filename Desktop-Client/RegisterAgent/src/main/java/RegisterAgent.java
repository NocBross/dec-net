package main.java;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import main.java.agent.CustomAgent;
import main.java.agent.CustomBottomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;
import main.java.constants.ClientLogs;

public class RegisterAgent extends CustomBottomAgent {

    public RegisterAgent(CustomAgent parent) throws Exception {
        super(parent, AgentID.REGISTER_AGENT, ClientLogs.REGISTER_AGENT, "RegisterAgent");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/RegisterScene.fxml"));
            rootSceneNode = loader.load();
            agentSceneController = (RootController) loader.getController();
            agentSceneController.setAgent(this);
            agentSceneController.setLogger(logger);
        } catch(IOException ioe) {
            logger.writeLog(logID + " error while loading RegisterAgent", ioe);
        }
    }
}
