package com.jackw.controllers;

import com.jackw.ManualMapper;
import com.jackw.logic.ApiInterface;
import com.jackw.logic.JavaField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

public class Tab2Getters {

	@FXML public Label label_steps;
	@FXML public Label label_steps_fixed;
	@FXML public Label label_locked;
	@FXML public AnchorPane anchor_pane_root;
	@FXML public ChoiceBox<ApiInterface> box_accessor;
	@FXML public ChoiceBox<JavaField> box_api_fields;
	@FXML public ChoiceBox<ApiInterface> box_client_fields;
	@FXML public ChoiceBox<ApiInterface> box_client_all_fields;

	public void setMain(ManualMapper main) {
		this.main = main;
	}

	private ManualMapper main;

	@FXML
	public void initialize() {
		anchor_pane_root.getChildren().stream().forEach(c -> {
			if (c != label_locked) {
				c.setDisable(true);
				c.setOpacity(0.15);
			}
		});
		System.out.println("set all components on this anchor pane to disabled + opacity=0.15");

		box_accessor.setConverter(new StringConverter<>() {
			@Override
			public String toString(ApiInterface object) {
				return object.name;
			}

			@Override
			public ApiInterface fromString(String string) {
				return box_accessor.getItems().filtered(apiInterface -> apiInterface.name.equals(string)).get(0);
			}
		});

		box_accessor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			box_api_fields.setDisable(false);
			box_api_fields.setItems(FXCollections.observableArrayList(newValue.entries));
			//main.tab1().table1.getItems().filtered(pbLink -> pbLink.getApiClass()); // TODO
		});

		box_api_fields.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			box_client_fields.setDisable(false);
			box_client_fields.setItems(FXCollections.observableArrayList());
			box_client_all_fields.setDisable(false);
			box_client_all_fields.setItems(FXCollections.observableArrayList());
		});
		System.out.println("[Tab2 Controller] Init complete");

	}

	public void unlockPanel() {
		label_locked.setVisible(false);
		anchor_pane_root.getChildren().stream().forEach(c -> {
			if (c != label_locked) {
				c.setDisable(false);
				c.setOpacity(1);
			}
		});
	}

	@FXML void onAction(ActionEvent e) {
		System.out.println(e.toString());
	}

	public void onTabOpened() {
		box_accessor.setItems(FXCollections.observableArrayList(main.tab1().list1.getItems()));
	}
}
