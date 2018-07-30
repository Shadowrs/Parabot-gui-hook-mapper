package com.jackw.controllers;

import com.jackw.ManualMapper;
import com.jackw.model.dummyapi.ApiInterface;
import com.jackw.model.dummyapi.ClientClass;
import com.jackw.model.dummyapi.JavaField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.controlsfx.control.Notifications;
import org.parabot.api.output.Logger;

public class Tab2Controller {

	@FXML public Label label_steps_fixed;
	@FXML public Label label_locked;
	@FXML public AnchorPane rootpane;
	@FXML public ChoiceBox<ApiInterface> box_accessor;
	@FXML public ChoiceBox<JavaField> box_api_fields;
	@FXML public ChoiceBox<JavaField> box_client_fields_typed;
	@FXML public ChoiceBox<JavaField> box_client_all_fields;
	@FXML public TextField text_client_class;
	@FXML public Button button_bind_getter;
	@FXML public Label label_api_fields;
	@FXML public Label label_client_fields_typed;
	@FXML public Label label_all_client_fields;
	@FXML public CheckBox tickbox_static;
	@FXML public Button button_setter;

	public void setMain(ManualMapper main) {
		this.main = main;
	}

	private ManualMapper main;
	private ClientClass current_assoc_class;

	@FXML
	public void initialize() {
		rootpane.getChildren().stream().filter(c -> c != label_locked).forEach(c -> {
			c.setDisable(true);
			c.setOpacity(0.15);
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

		box_api_fields.setConverter(new StringConverter<>() {
			@Override
			public String toString(JavaField object) {
				return object.getDisplayForASMType();
			}

			@Override
			public JavaField fromString(String string) {
				System.out.println("when "+string);
				return box_api_fields.getItems().filtered(f -> f.name.equals(string)).get(0);
			}
		});

		box_client_fields_typed.setConverter(new StringConverter<>() {
			@Override
			public String toString(JavaField object) {
				return object.getDisplayForASMType();
			}

			@Override
			public JavaField fromString(String string) {
				return box_client_fields_typed.getItems().filtered(f -> f.name.equals(string)).get(0);
			}
		});

		box_client_all_fields.setConverter(new StringConverter<>() {
			@Override
			public String toString(JavaField object) {
				return object.getDisplayForASMType();
			}

			@Override
			public JavaField fromString(String string) {
				return box_client_all_fields.getItems().filtered(f -> f.name.equals(string)).get(0);
			}
		});

		box_accessor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				current_assoc_class = null;
				//System.err.println("how does this happen "+oldValue.name);
				return;
			}
			box_api_fields.setDisable(false);
			ObservableList<JavaField> list = newValue.getMethods();
			box_api_fields.setItems(list);
			label_api_fields.setText("2. Available API methods ("+list.size()+") :");
			label_client_fields_typed.setText("3. Available Client Fields by Type:");

			System.out.println(newValue.name+" v "+ Arrays.toString((String[])main.tab1().table1.getItems().stream()
					.map(Tab1Controller.InterfaceBind::getApiClass)
					.collect(Collectors.toList())
					.toArray(new String[0])));

			Tab1Controller.InterfaceBind ib = main.tab1().table1.getItems()
					.filtered(pbLink -> pbLink.getApiClass().equals(newValue.name))
					.get(0);
			//Logger.info("Tab2Controller", "Found bind: "+ib);

			box_client_all_fields.setDisable(false);

			long s1 = System.currentTimeMillis();

			current_assoc_class = main.data.client.entries.stream().filter(c -> c.name.equals(ib.getClientClass())).findFirst().get();
			list = current_assoc_class.getFields();

			if (list.size() > 100)
				Logger.warning("Tab2Controller", "Generated List<JField> in "+(System.currentTimeMillis()-s1)+"ms");

			long s = System.currentTimeMillis();
			if (list.size() > 100) {
				Logger.warning("Tab2Controller", "Setting inner Items of ChoiceBox - but count is "+list.size()+" -- this can be slow!");
			}

			box_client_all_fields.setItems(list);

			if (list.size() > 100)
				Logger.warning("Tab2Controller", "ChoiceBox Item list set in "+(System.currentTimeMillis()-s)+"ms");

			text_client_class.setText(ib.getClientClass());

			label_all_client_fields.setText("3. Alternative: All "+text_client_class.getText()+" Fields ("+
					box_client_all_fields.getItems().size()+") :");
		});

		box_api_fields.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) return;
			box_client_fields_typed.setDisable(false);


			List<JavaField> list = current_assoc_class.getFields().stream().filter(newValue::typeMatch).collect(Collectors.toList());

			// [Getters] Look for mapped accessor->client class matches
			String accessorType = newValue.descToTypeOnly();
			List<Tab1Controller.InterfaceBind> alts = main.tab1().table1.getItems().stream().filter(ib -> ib.getApiClass().equals(accessorType)).collect(Collectors.toList());
			if (alts.size() > 0) {
				// will only be 1 match
				final String clientClass = alts.get(0).getClientClass();
				list.addAll(current_assoc_class.getFields().stream().filter(v -> v.isType(clientClass)).collect(Collectors.toList()));
			}

			// [Setters] Look for any method with one argument matching the type. Return must be void.. not sure if
			// Parabot supports return types of Types. would always return a null value anyway unless you insert a chain reference
			if (newValue.descToTypeOnly().equals("Void") && newValue.hasExactArgCount(1)) {
				alts = main.tab1().table1.getItems().stream().filter(ib -> ib.getApiClass().equals(newValue.argsToString())).collect(Collectors.toList());
				if (alts.size() > 0) {
					final String clientClass = alts.get(0).getClientClass();
					// The paramater is the same accessor
					list.addAll(current_assoc_class.getFields().stream().filter(v -> v.isType(clientClass)).collect(Collectors.toList()));
				}
			}
			System.out.println("Search complete for matches of "+newValue.getDisplayForASMType()+" // "+accessorType+" // "+newValue.argCount()+" // "+newValue.argsToString());

			box_client_fields_typed.setItems(FXCollections.observableArrayList(list));

			label_client_fields_typed.setText("3. "+ text_client_class.getText()+" Fields by Type ("+
					box_client_fields_typed.getItems().size()+") :");

		});

		box_client_fields_typed.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null)
			if (-1 != box_client_all_fields.getSelectionModel().getSelectedIndex())
				box_client_all_fields.getSelectionModel().clearSelection();
		});

		box_client_all_fields.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null)
			if (-1 != box_client_fields_typed.getSelectionModel().getSelectedIndex())
				box_client_fields_typed.getSelectionModel().clearSelection();
		});

		System.out.println("[Tab2 Controller] Init complete");

	}

	public void unlockPanel() {
		label_locked.setVisible(false); // hide warning
		rootpane.getChildren().stream().filter(c -> c != label_locked).forEach(c -> c.setOpacity(1));
		box_accessor.setDisable(false); // enable the first box
		button_bind_getter.setDisable(false);

	}

	public void lockPanel() {
		label_locked.setVisible(true); // hide warning
		rootpane.getChildren().stream().filter(c -> c != label_locked).forEach(c -> {
			c.setOpacity(0.15);
			c.setDisable(true);
		});
	}

	@FXML void onAction(ActionEvent e) {
		if (e.getTarget() == button_bind_getter) {
			JavaField apiField = box_api_fields.getSelectionModel().getSelectedItem();
			if (apiField == null) {
				Notifications.create().text("You need to select an API Field to bind!").showWarning();
				return;
			}
			JavaField field = box_client_all_fields.getSelectionModel().getSelectedItem() != null ?
			box_client_all_fields.getSelectionModel().getSelectedItem() :
			box_client_fields_typed.getSelectionModel().getSelectedItem();
			if (field == null) {
				Notifications.create().text("You need to select a Client Field to bind!").showWarning();
				return;
			}
			System.out.println("field: "+field.name+" to "+apiField.name);
		}
		System.out.println(e.toString());
	}

	public void onTabOpened() {
		main.resizeTo(rootpane.getPrefWidth() + 15, rootpane.getPrefHeight() + 60);
		if (main.tab1().table1.getItems().size() == 0) {
			lockPanel();
			return;
		}
	}

	public void clearSelections() {
		updateAccessorsListItems();
		box_api_fields.getSelectionModel().clearSelection();
		box_api_fields.setItems(FXCollections.observableArrayList());
		box_client_fields_typed.getSelectionModel().clearSelection();
		box_client_fields_typed.setItems(FXCollections.observableArrayList());
		box_client_all_fields.getSelectionModel().clearSelection();
		box_client_all_fields.setItems(FXCollections.observableArrayList());
		text_client_class.setText("?");
		label_api_fields.setText("2. Available API methods:");
		label_client_fields_typed.setText("3. Available Client Fields by Type:");
		label_all_client_fields.setText("3. Alternative: All Client Class Fields:");
	}

	public void updateAccessorsListItems() {
		// Map The Column cells in Tab1 Table1 (String: Accessor names) to actual accessor instances in list1.
		List<ApiInterface> items = main.tab1().table1.getItems().stream()
				.map(Tab1Controller.InterfaceBind::getApiClass)
				.map(e -> main.tab1().list1.getItems().stream()
						.filter(i -> i.name.equals(e)).findFirst().get())
				.collect(Collectors.toCollection(() -> new ArrayList<>(0)));
		box_accessor.getSelectionModel().clearSelection(); // otherwise NPE thrown by onSelectionChanged() due to null new val
		box_accessor.setItems(FXCollections.observableArrayList(items));
	}
}
