package com.jackw;

import com.jackw.controllers.Tab1Controller;
import com.jackw.controllers.Tab2Getters;
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
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/tab1.fxml"));

		final Scene scene = new Scene(fxmlLoader.load());
		final Tab1Controller controller1 = fxmlLoader.getController();
		// any on init code here


		final FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("views/tab2_getters.fxml"));
		controller1.tab_getters.setContent(fxmlLoader2.load());
		final Tab2Getters controller2 = fxmlLoader2.getController();

		stage.setTitle("Parabot Mapper");
		stage.setScene(scene);
		stage.show();

	}
}
