package com.jackw;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ManualMapper extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cfg.fxml"));

		Scene scene = new Scene(fxmlLoader.load());
		Controller controller = fxmlLoader.getController();
		// any on init code here

		stage.setTitle("Parabot Mapper");
		stage.setScene(scene);
		stage.show();

	}
}
