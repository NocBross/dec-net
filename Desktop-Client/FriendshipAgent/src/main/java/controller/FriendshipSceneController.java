package main.java.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import main.java.abstraction.FriendshipSceneComponents;
import main.java.agent.CustomAgent;
import main.java.agent.RootController;
import main.java.constants.Port;
import main.java.constants.WebServiceConstants;

public class FriendshipSceneController extends FriendshipSceneComponents implements RootController {

    private CustomAgent agent;
    private ObservableList<String> directMessageListItems;

    @FXML
    public void initialize() {
        agent = null;
        directMessageList = new ListView<String>();
        directMessageListItems = FXCollections.observableArrayList();
        directMessageList.setItems(directMessageListItems);
        directMessagePane.setContent(directMessageList);
        scrollPane.setVvalue(-0.05);
    }

    @Override
    public void receiveResult(String message) {
        try {
            if (message.indexOf(WebServiceConstants.RESULT_SEPARTOR) != -1) {
                List<String> resultList = new LinkedList<String>();
                String[] results = message.split(WebServiceConstants.RESULT_SEPARTOR);

                for (int i = 0; i < results.length; i++) {
                    if (!directMessageListItems.contains(results[i])) {
                        resultList.add(results[i]);
                    }
                }

                final FutureTask<String> dialog = new FutureTask<String>(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        ChoiceDialog<String> dialog = new ChoiceDialog<>(resultList.get(0), resultList);
                        dialog.setTitle("Suche");
                        dialog.setHeaderText("Gefundene Personen");
                        dialog.setContentText("Nickname:");

                        Optional<String> result = dialog.showAndWait();
                        if (result.isPresent()) {
                            return result.get();
                        }
                        return null;
                    }
                });
                Platform.runLater(dialog);
                directMessageListItems.add(dialog.get());
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void setAgent(CustomAgent newAgent) {
        agent = newAgent;
    }

    /**
     * Sets the courser to a hand when the mouse is over the search label.
     * 
     * @param event
     *            - mouse entered event
     */
    @FXML
    protected void hover(MouseEvent event) {
        searchLabel.setCursor(Cursor.HAND);
    }

    /**
     * Sets the courser to the default courser when the search label is leaved.
     * 
     * @param event
     *            - mouse exited event
     */
    @FXML
    protected void noHover(MouseEvent event) {
        searchLabel.setCursor(Cursor.DEFAULT);
    }

    /**
     * Opens the search dialog.
     * 
     * @param event
     *            - mouse clicked
     */
    @FXML
    protected void search(MouseEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Suche");
        dialog.setHeaderText("Neuen Kontakt per Nickname suchen.");
        dialog.setContentText("Nickname:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String url = "http://localhost:" + Port.WEBSERVICE + "/search" + WebServiceConstants.SEARCH_SEPARATOR
                    + WebServiceConstants.SEARCH_NICKNAME_KEY + WebServiceConstants.KEY_VALUE_SEPARATOR + result.get();
            agent.sendMessage(url, null);
        }
    }

}
