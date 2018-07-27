package com.jackw.controllers;

import com.jackw.ManualMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class MainController {

	@FXML public AnchorPane rootpane;
	@FXML public TabPane tabpane1;
	@FXML public Tab tab1;
	@FXML public Tab tab2;

	private ManualMapper main;

	public void setMain(ManualMapper main) {
		this.main = main;
	}

	@FXML public void initialize() {
		tabpane1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == tab2) {
				main.tab2().onTabOpened();
			} else if (newValue == tab1) {
				main.tab1().onTabOpened();
			}
		});
	}
}
