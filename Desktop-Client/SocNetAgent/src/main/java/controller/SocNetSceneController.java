package main.java.controller;

import java.util.List;

import main.java.abstraction.SocNetSceneComponents;
import main.java.agent.CustomAgent;

public class SocNetSceneController extends SocNetSceneComponents {

    public void loadChildren(List<CustomAgent> children) {
        horizontalSplitPane.getItems().add(0, children.get(0).getScene());
        verticalSplitPane.getItems().add(0, children.get(1).getScene());
    }

}
