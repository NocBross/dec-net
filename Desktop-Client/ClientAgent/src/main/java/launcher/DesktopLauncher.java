package main.java.launcher;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.client_agent.ClientAgent;

public class DesktopLauncher extends Application {

	/**
	 * Entry point of the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientAgent clientAgent = new ClientAgent(primaryStage);

		primaryStage.setTitle("Lock-That");
		primaryStage.setScene(new Scene(clientAgent.getScene()));
		primaryStage.setMinHeight(700);
		primaryStage.setMinWidth(450);
		primaryStage.centerOnScreen();
		primaryStage.setMaximized(true);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				clientAgent.close();
			}

		});

		primaryStage.show();
	}
}
