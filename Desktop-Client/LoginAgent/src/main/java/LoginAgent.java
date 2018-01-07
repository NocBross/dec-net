package main.java;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import main.java.agent.CustomAgent;
import main.java.agent.CustomBottomAgent;
import main.java.agent.RootController;
import main.java.constants.AgentID;
import main.java.constants.ClientLogs;

public class LoginAgent extends CustomBottomAgent {

    public LoginAgent(CustomAgent parent) throws Exception {
        super(parent, AgentID.LOGIN_AGENT, ClientLogs.LOGIN_AGENT, "LoginAgent");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/LoginScene.fxml"));
            rootSceneNode = loader.load();
            agentSceneController = (RootController) loader.getController();
            agentSceneController.setAgent(this);
            agentSceneController.setLogger(logger);
        } catch(IOException ioe) {
            logger.writeLog(logID + " error while loading LoginAgent", ioe);
        }
    }

}
