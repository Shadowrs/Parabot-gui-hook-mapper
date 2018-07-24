package com.jackw;

import com.jackw.controllers.Tab1Controller;
import com.jackw.controllers.Tab2Getters;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ManualMapper extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public enum CTRLS {
		TAB1,
		TAB2
	}

	public static Map<CTRLS, Object> controllers = new HashMap<>(2);

	@Override
	public void start(Stage stage) throws IOException {
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/tab1.fxml"));

		final Scene scene = new Scene(fxmlLoader.load());
		final Tab1Controller controller1 = fxmlLoader.getController();
		controllers.put(CTRLS.TAB1, controller1);
		// any on init code here


		final FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("views/tab2_getters.fxml"));
		controller1.tab_getters.setContent(fxmlLoader2.load());
		final Tab2Getters controller2 = fxmlLoader2.getController();
		controllers.put(CTRLS.TAB2, controller2);

		stage.setTitle("Parabot Mapper");
		stage.setScene(scene);
		stage.show();

	}
}
