package com.jackw.controllers;

import com.jackw.ManualMapper;
import com.jackw.model.*;
import java.io.File;
import java.util.Random;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.controlsfx.control.Notifications;

public class Tab1Controller {

	@FXML public Label lblApiPath;
	@FXML public Label lblApiPath1;
	@FXML public Button select_jar_api;
	@FXML public Button select_jar_client;
	@FXML public Button load_jar_api;
	@FXML public Button load_jar_client;
	@FXML public ListView<ApiInterface> list1;
	@FXML public ListView<ClientEntry> list2;
	@FXML public TableView<InterfaceBind> table1;
	@FXML public MenuItem menuitem_link_api_interface;
	@FXML public MenuItem menuitem_link_class;
	@FXML public TableColumn<InterfaceBind, String> column1_apiInterClass;
	@FXML public TableColumn<InterfaceBind, String> column2_clientClass;
	@FXML public Tab tab_interfaces;
	@FXML public Tab tab_getters;
	@FXML public Tooltip tt_clientjarpath;
	@FXML public Tooltip tt_apijarpath;
	@FXML public TabPane tabpane1;

	public void setMain(ManualMapper main, ApiData data) {
		this.main = main;
		this.data = data;
	}

	private ManualMapper main;
	private ApiData data;

	@FXML
	void onAction(ActionEvent e) {
		EventTarget target = e.getTarget();
		assert target != null;
		if (target == select_jar_api) {
			data.pbApi = new PbApi(data, new File("api.jar"));
			lblApiPath.setText(data.pbApi.apiJar.getAbsolutePath());
			Notifications.create().title("Parabot Mapper").text("Loaded API JAR").show();
		} else if (target == select_jar_client) {
			data.client = new RspsClient(new File("dreamscape.jar"));
			lblApiPath1.setText(data.client.client.getAbsolutePath());
			Notifications.create().title("Parabot Mapper").text("Loaded Client JAR").show();
		}
		else if (target == load_jar_api) {
			if (data.pbApi == null) {
				lblApiPath.setText("No Api JAR selected!");
			} else {
				list1.setItems(FXCollections.observableArrayList(data.pbApi.interfaces));
				tt_apijarpath.setText("Loaded at: "+data.pbApi.apiJar.getAbsolutePath());
				Notifications.create().title("Parabot Mapper").text("Reloaded API JAR-" + new Random().nextInt(100)).show();
				onLoadJar();
			}
		}
		else if (target == load_jar_client) {
			if (data.client == null) {
				lblApiPath1.setText("No Client JAR selected!");
			} else {
				list2.setItems(FXCollections.observableArrayList(data.client.entries));
				tt_clientjarpath.setText("Loaded at: "+data.client.client.getAbsolutePath());
				Notifications.create().title("Parabot Mapper").text("Reloaded Client JAR-" + new Random().nextInt(100)).show();
				onLoadJar();
			}
		} else if (target == menuitem_link_api_interface) {
			link();
		} else if (target == menuitem_link_class) {
			link();
		} else {
			System.out.println(target.toString());
			Notifications.create().title("Parabot Mapper").text("Action missing: "+target.toString()).show();
		}
	}

	private void onLoadJar() {
	}

	private void link() {
		ApiInterface x = list1.getSelectionModel().getSelectedItem();
		ClientEntry c = list2.getSelectionModel().getSelectedItem();
		if (x == null || c == null) {
			System.out.println("bad selection");
			Notifications.create().title("Error").text(String.format("You need to select an %s",
			x == null ? "API Interface class" : "Client class")).showError();
		} else {
			table1.getItems().add(new InterfaceBind(x, c));
			System.out.println("added new PbLink. Total now "+table1.getItems().size()+". Columns: "+table1.getColumns().size());
			main.tab2().unlockPanel();
		}
	}

	public static class InterfaceBind {
		private final SimpleStringProperty apiClass;
		private final SimpleStringProperty clientClass;

		public InterfaceBind(ApiInterface apiInterface, ClientEntry clientEntry) {
			apiClass = new SimpleStringProperty(apiInterface.name);
			clientClass = new SimpleStringProperty(clientEntry.name);
		}

		public String getApiClass() {
			return apiClass.get();
		}

		public SimpleStringProperty apiClassProperty() {
			return apiClass;
		}

		public String getClientClass() {
			return clientClass.get();
		}

		public SimpleStringProperty clientClassProperty() {
			return clientClass;
		}
	}

	@FXML public void initialize() {
		System.out.println("initialzing!");

		column1_apiInterClass.setCellValueFactory(new PropertyValueFactory<>("apiClass"));
		column2_clientClass.setCellValueFactory(new PropertyValueFactory<>("clientClass"));
		createContextMenuCellFactory(column1_apiInterClass);
		createContextMenuCellFactory(column2_clientClass);

		list1.setCellFactory(new Callback<>() {
			@Override
			public ListCell<ApiInterface> call(ListView<ApiInterface> param) {
				return new ListCell<>() {
					@Override
					protected void updateItem(ApiInterface item, boolean empty) {
						if (item == null) return;
						super.updateItem(item, empty);
						setText(item.name);
					}
				};
			}
		});

		list2.setCellFactory(new Callback<>() {
			@Override
			public ListCell<ClientEntry> call(ListView<ClientEntry> param) {
				return new ListCell<>() {
					@Override
					protected void updateItem(ClientEntry item, boolean empty) {
						if (item == null) return;
						super.updateItem(item, empty);
						setText(item.name);
					}
				};
			}
		});

		tabpane1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == tab_getters) {
				main.tab2().onTabOpened();
			}
		});

		// We're ready
		System.out.println("ready");
	}

	private void createContextMenuCellFactory(TableColumn<InterfaceBind, String> col) {
		final StringProperty contextMenuValue = new SimpleStringProperty("");
		final ContextMenu menu = new ContextMenu();
		final MenuItem mi3 = new MenuItem();
		// text depends on "current cell"
		// more commonly, the action handler would depend on this value,
		// but this is just a proof of concept
		mi3.textProperty().bind(Bindings.format("Unbind %s", contextMenuValue));
		menu.getItems().addAll(mi3);

		col.setCellFactory(new Callback<>() {
			@Override
			public TableCell<InterfaceBind, String> call(TableColumn<InterfaceBind, String> param) {

				TableCell<InterfaceBind, String> cell = new TableCell<>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							setText(item);
						}
					}
				};
				cell.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
					if (event.isSecondaryButtonDown() && !cell.isEmpty()) {
						contextMenuValue.setValue(cell.getItem());
						menu.show(cell, event.getScreenX(), event.getScreenY());
					}
				});
				return cell;
			}
		});

	}

}
