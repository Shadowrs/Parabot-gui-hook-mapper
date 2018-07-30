package com.jackw;

import com.jackw.controllers.MainController;
import com.jackw.controllers.Tab1Controller;
import com.jackw.controllers.Tab2Controller;
import com.jackw.model.dummyapi.ApiData;
import com.jackw.model.dummyapi.PbApi;
import com.jackw.model.dummyapi.RspsClient;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ManualMapper extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	// fast
	private Tab1Controller t1_controller;
	private Tab2Controller t2_controller;
	private MainController mainController;
	public List<Path> recentFiles = new ArrayList<>(0);
	public List<Path> recentFiles2 = new ArrayList<>(0);

	public Stage getStage() {
		return stage;
	}

	private Stage stage;

	@Override
	public void start(Stage stage) throws IOException {

		// Load main fxml
		final FXMLLoader main = new FXMLLoader(getClass().getResource("views/main.fxml"));

		// Init scene
		final Scene scene = new Scene(main.load());
		scene.getStylesheets().add("/com/jackw/views/css/hi.css");

		final MainController mainController = this.mainController = main.getController();
		mainController.setMain(this);


		// Init tab1
		final FXMLLoader tab1 = new FXMLLoader(getClass().getResource("views/tab1.fxml"));
		mainController.tab1.setContent(tab1.load());
		final Tab1Controller tab1_controller = t1_controller = tab1.getController();
		tab1_controller.setMain(this);

		// Init tab2
		final FXMLLoader tab2 = new FXMLLoader(getClass().getResource("views/tab2.fxml"));
		mainController.tab2.setContent(tab2.load());
		final Tab2Controller tab2_controller = t2_controller = tab2.getController();
		tab2_controller.setMain(this);

		this.stage = stage;
		stage.setTitle("Parabot Mapper");
		stage.setScene(scene);
		stage.getIcons().add(new Image(ManualMapper.class.getResourceAsStream("/parabot-icon.png")));
		resizeTo(tab1().pane.getPrefWidth() + 15, tab1().pane.getPrefHeight() + 60);
		stage.show();

		recentFiles.add(new File(
				System.getProperty("user.home")+ File.separator+"desktop"+File.separator+"pb-317-min-api.jar")
				.toPath()); // dummy, fine with not existing
		recentFiles.add(Paths.get("C:\\Users\\Jak\\Documents\\Parabot\\cache/RuneWild-api-2454733162.jar"));
		recentFiles.add(Paths.get("C:\\Users\\Jak\\.m2\\repository\\org\\parabot\\317-api-minified\\1.21.3\\317-api-minified-1.21.3.jar"));

		recentFiles2.add(new File(
				System.getProperty("user.home")+ File.separator+"desktop"+File.separator+"dreamscape.jar")
				.toPath());
		recentFiles2.add(Paths.get("C:\\Users\\Jak\\Documents\\Parabot\\cache/RuneWild-client-2441707740.jar"));

		// Fast populate for testing
		data.pbApi = new PbApi(recentFiles.get(0).toFile());
		data.client = new RspsClient(recentFiles2.get(0).toFile());

		tab1_controller.list1.getItems().addAll(data.pbApi.interfaces);
		tab1_controller.list2.getItems().addAll(data.client.entries);

		for (int i = 0; i < 3; i++) {
			tab1().list1.getSelectionModel().select(i);
			tab1().list2.getSelectionModel().select(i);
			tab1().bind();
		}

		tab2_controller.unlockPanel();


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

	public void resizeTo(double width, double height) {
		/*getStage().setWidth(width);
		getStage().setHeight(height);*/
	}
}
