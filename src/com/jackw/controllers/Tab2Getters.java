package com.jackw.controllers;

import com.jackw.ManualMapper;
import com.jackw.logic.ApiInterface;
import com.jackw.logic.JavaField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

public class Tab2Getters {

	@FXML public Label label_steps_fixed;
	@FXML public Label label_locked;
	@FXML public AnchorPane anchor_pane_root;
	@FXML public ChoiceBox<ApiInterface> box_accessor;
	@FXML public ChoiceBox<JavaField> box_api_fields;
	@FXML public ChoiceBox<ApiInterface> box_client_fields;
	@FXML public ChoiceBox<ApiInterface> box_client_all_fields;
	@FXML public TextField txt_client_class;

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

		box_api_fields.setConverter(new StringConverter<JavaField>() {
			@Override
			public String toString(JavaField object) {
				return object.name;
			}

			@Override
			public JavaField fromString(String string) {
				return  box_api_fields.getItems().filtered(f -> f.name.equals(string)).get(0);
			}
		});

		box_accessor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			box_api_fields.setDisable(false);
			box_api_fields.setItems(FXCollections.observableArrayList(newValue.entries));
			System.out.println(newValue.name+" v "+ Arrays.toString((String[])main.tab1().table1.getItems().stream()
					.map(s -> s.getApiClass())
					.collect(Collectors.toList())
					.toArray(new String[0])));
			Tab1Controller.InterfaceBind ib = main.tab1().table1.getItems()
					.filtered(pbLink -> pbLink.apiClassProperty().get().equals(newValue.name))
					.get(0);
			txt_client_class.setText(ib.getClientClass());
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
		label_locked.setVisible(false); // hide warning
		anchor_pane_root.getChildren().stream().forEach(c -> {
			if (c != label_locked) {
				c.setOpacity(1);
			}
		});
		box_accessor.setDisable(false); // enable the first box
	}

	public void lockPanel() {
		label_locked.setVisible(true); // hide warning
		anchor_pane_root.getChildren().stream().forEach(c -> {
			if (c != label_locked) {
				c.setOpacity(0.15);
				c.setDisable(true);
			}
		});
	}

	@FXML void onAction(ActionEvent e) {
		System.out.println(e.toString());
	}

	public void onTabOpened() {
		if (main.tab1().table1.getItems().size() == 0) {
			lockPanel();
			return;
		}
		List<ApiInterface> items = new ArrayList<>(0);
		main.tab1().table1.getItems().stream().map(e -> e.getApiClass()).forEach(e -> {
			items.add(main.tab1().list1.getItems().stream().filter(i -> i.name.equals(e)).findFirst().get());
		});
		box_accessor.setItems(FXCollections.observableArrayList(items));
	}
}
