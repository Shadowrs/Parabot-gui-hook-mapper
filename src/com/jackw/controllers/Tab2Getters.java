package com.jackw.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class Tab2Getters {

	@FXML Label label_steps;
	@FXML Label label_steps_fixed;
	@FXML Label label_locked;
	@FXML AnchorPane anchor_pane_root;

	@FXML
	public void initalize() {
		anchor_pane_root.getChildren().stream().forEach(c -> {
			if (c != label_locked) {
				c.setDisable(true);
				c.setOpacity(0.12);
			}
		});
		System.out.println("set all comopnents on this anchor pane to disabled + opacity=0.12");
	}
}
