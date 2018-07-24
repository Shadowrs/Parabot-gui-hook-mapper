package com.jackw.controllers;

import com.jackw.logic.ApiInterface;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class Tab2Getters {

	@FXML Label label_steps;
	@FXML Label label_steps_fixed;
	@FXML Label label_locked;
	@FXML AnchorPane anchor_pane_root;
	@FXML ChoiceBox<ApiInterface> accessorChoice;

	@FXML
	public void initialize() {
		anchor_pane_root.getChildren().stream().forEach(c -> {
			if (c != label_locked) {
				c.setDisable(true);
				c.setOpacity(0.15);
			}
		});
		System.out.println("set all components on this anchor pane to disabled + opacity=0.15");
	}

}
