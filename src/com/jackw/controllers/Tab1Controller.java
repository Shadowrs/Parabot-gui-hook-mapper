package com.jackw.controllers;

import com.jackw.logic.*;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.controlsfx.control.Notifications;

public class Tab1Controller {

	@FXML private Label lblApiPath;
	@FXML private Label lblApiPath1;
	@FXML private Button select_jar_api;
	@FXML private Button select_jar_client;
	@FXML private Button load_jar_api;
	@FXML private Button load_jar_client;
	@FXML private ListView<ListEntryAdapter<ApiInterface>> list1;
	@FXML private ListView<ClientEntry> list2;
	@FXML private TableView<PbLink> table1;
	@FXML MenuItem menuitem_link_api_interface;
	@FXML MenuItem menuitem_link_class;
	@FXML TableColumn<PbLink, String> apiClass;
	@FXML TableColumn<PbLink, String> clientClass;
	@FXML public Tab tab_interfaces;
	@FXML public Tab tab_getters;

	private ApiData data = new ApiData();

	@FXML
	void onAction(ActionEvent e) {
		EventTarget target = e.getTarget();
		assert target != null;
		if (target == select_jar_api) {
			data.pbApi = new PbApi(data, new File("api.jar"));
			lblApiPath.setText(data.pbApi.apiJar.getAbsolutePath());
			List<ListEntryAdapter<ApiInterface>> list = data.pbApi.interfaces.stream()
					.map(i -> new ListEntryAdapter<>(i.getClass().getName(), i))
					.collect(Collectors.toList());
			list1.setItems(FXCollections.observableArrayList(list));
			Notifications.create().title("Parabot Mapper").text("Loaded API JAR").show();
		} else if (target == select_jar_client) {
			data.client = new RspsClient(new File("dreamscape.jar"));
			lblApiPath1.setText(data.client.client.getAbsolutePath());
			list2.setItems(FXCollections.observableArrayList(data.client.entries));
			Notifications.create().title("Parabot Mapper").text("Loaded Client JAR").show();
		}
		else if (target == load_jar_api) {
			if (data.pbApi == null) {
				lblApiPath.setText("No Api JAR selected!");
			} else {
				Notifications.create().title("Parabot Mapper").text("Reloaded API JAR-" + new Random().nextInt(100)).show();
			}
		}
		else if (target == load_jar_client) {
			if (data.client == null) {
				lblApiPath1.setText("No Client JAR selected!");
			} else {
				Notifications.create().title("Parabot Mapper").text("Reloaded Client JAR-" + new Random().nextInt(100)).show();
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

	private void link() {
		ListEntryAdapter<ApiInterface> x = list1.getSelectionModel().getSelectedItem();
		ClientEntry c = list2.getSelectionModel().getSelectedItem();
		if (x == null || c == null) {
			System.out.println("bad selection");
			Notifications.create().title("Error").text(String.format("You need to select an %s",
			x == null ? "API Interface class" : "Client class")).showError();
		} else {
			table1.getItems().add(new PbLink(x.getO().getClass().getSimpleName(), c.name));
			System.out.println("added new PbLink. Total now "+table1.getItems().size()+". Columns: "+table1.getColumns().size());
		}
	}

	public static class PbLink {
		private final SimpleStringProperty apiClass;
		private final SimpleStringProperty clientClass;

		public PbLink(String c1, String c2) {
			apiClass = new SimpleStringProperty(c1);
			clientClass = new SimpleStringProperty(c2);
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

		apiClass.setCellValueFactory(new PropertyValueFactory<>("apiClass"));
		clientClass.setCellValueFactory(new PropertyValueFactory<>("clientClass"));

		list1.setCellFactory(new Callback<>() {
			@Override
			public ListCell<ListEntryAdapter<ApiInterface>> call(ListView<ListEntryAdapter<ApiInterface>> param) {
				return new ListCell<>() {
					@Override
					protected void updateItem(ListEntryAdapter<ApiInterface> item, boolean empty) {
						if (item == null) return;
						super.updateItem(item, empty);
						setText(item.getO().getClass().getSimpleName());
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

		final MenuItem test = new MenuItem("test");

		// Configure context menu of a selected item
		table1.setRowFactory(tv -> {
			TableRow<PbLink> row = new TableRow<>();
			ContextMenu menu = new ContextMenu();

			row.setOnContextMenuRequested((event) -> {
				if(! row.isEmpty()) {
					if(! row.getContextMenu().getItems().contains(test)) {
						row.getContextMenu().getItems().add(0, test);
					}
				}
			});

			row.itemProperty().addListener((obs, oldItem, newItem) -> {

			});

			row.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) ->
					row.setContextMenu(isNowEmpty ? null : menu));
			return row ;
		});

		// We're ready
		System.out.println("ready");
	}

	public class ListEntryAdapter<T> {
		private final SimpleStringProperty e; // TODO could actually just be a string i think
		// no need for this wrapper
		private T o;

		ListEntryAdapter(String display, T o) {
			e = new SimpleStringProperty(display);
			this.o = o;
		}

		public String getE() {
			return e.get();
		}

		public SimpleStringProperty eProperty() {
			return e;
		}

		public void setE(String e) {
			this.e.set(e);
		}

		public T getO() {
			return o;
		}

		public void setO(T o) {
			this.o = o;
		}
	}
}
