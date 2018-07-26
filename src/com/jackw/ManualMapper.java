package com.jackw;

import com.jackw.controllers.Tab1Controller;
import com.jackw.controllers.Tab2Controller;
import com.jackw.model.dummyapi.ApiData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ManualMapper extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	// fast
	private Tab1Controller t1_controller;
	private Tab2Controller t2_controller;
	public List<Path> recentFiles = new ArrayList<>(0);
	public List<Path> recentFiles2 = new ArrayList<>(0);

	public Stage getStage() {
		return stage;
	}

	private Stage stage;

	@Override
	public void start(Stage stage) throws IOException {
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/tab1.fxml"));

		final Scene scene = new Scene(fxmlLoader.load());
		scene.getStylesheets().add("/com/jackw/views/css/hi.css");

		final Tab1Controller controller1 = fxmlLoader.getController();
		controller1.setMain(this, data);
		t1_controller = controller1;
		// any on init code here


		final FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("views/tab2_getters.fxml"));
		controller1.tab_getters.setContent(fxmlLoader2.load());
		final Tab2Controller controller2 = fxmlLoader2.getController();
		controller2.setMain(this);
		t2_controller = controller2;

		stage.setTitle("Parabot Mapper");
		stage.setScene(scene);
		stage.show();
		this.stage = stage;

		recentFiles.add(new File(
				System.getProperty("user.home")+ File.separator+"desktop"+File.separator+"pb-317-min-api.jar")
				.toPath());
		recentFiles2.add(new File(
				System.getProperty("user.home")+ File.separator+"desktop"+File.separator+"dreamscape.jar")
				.toPath());
	}

	public ApiData getData() {
		return data;
	}

	public final ApiData data = new ApiData();

	public Tab1Controller tab1() {
		return t1_controller;
	}

	public Tab2Controller tab2() {
		return t2_controller;
	}

	public void resizeTo(Pane pane) {
		getStage().setWidth(pane.getWidth());
		getStage().setHeight(pane.getHeight());
	}

	public void resizeTo(int width, int height) {
		getStage().setWidth(width);
		getStage().setHeight(height);
	}
}
