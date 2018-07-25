package com.jackw;

import com.jackw.controllers.Tab1Controller;
import com.jackw.controllers.Tab2Getters;
import com.jackw.model.ApiData;
import com.jackw.model.ControllerKey;
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

	// slow
	public static Map<ControllerKey, Object> controllers = new HashMap<>(2);

	// fast
	private Tab1Controller t1_controller;
	private Tab2Getters t2_controller;

	@Override
	public void start(Stage stage) throws IOException {
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/tab1.fxml"));

		final Scene scene = new Scene(fxmlLoader.load());
		final Tab1Controller controller1 = fxmlLoader.getController();
		controllers.put(ControllerKey.TAB1, controller1);
		controller1.setMain(this, data);
		t1_controller = controller1;
		// any on init code here


		final FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("views/tab2_getters.fxml"));
		controller1.tab_getters.setContent(fxmlLoader2.load());
		final Tab2Getters controller2 = fxmlLoader2.getController();
		controllers.put(ControllerKey.TAB2, controller2);
		controller2.setMain(this);
		t2_controller = controller2;

		stage.setTitle("Parabot Mapper");
		stage.setScene(scene);
		stage.show();

	}

	public ApiData getData() {
		return data;
	}

	public final ApiData data = new ApiData();

	public <T> T ctrlr(ControllerKey key) {
		return (T) controllers.get(key);
	}

	public Tab1Controller tab1() {
		return t1_controller;
	}

	public Tab2Getters tab2() {
		return t2_controller;
	}
}
