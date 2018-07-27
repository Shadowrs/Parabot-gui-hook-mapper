package com.jackw.controllers;

import com.jackw.ManualMapper;
import com.jackw.model.dummyapi.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.controlsfx.control.Notifications;

public class Tab1Controller {

	@FXML public Label label_path_1;
	@FXML public Label label_path_2;
	@FXML public Button select_jar_api;
	@FXML public Button select_jar_client;
	@FXML public Button load_jar_api;
	@FXML public Button load_jar_client;
	@FXML public ListView<ApiInterface> list1;
	@FXML public ListView<ClientClass> list2;
	@FXML public TableView<InterfaceBind> table1;
	@FXML public MenuItem menuitem_link_api_interface;
	@FXML public MenuItem menuitem_link_class;
	@FXML public TableColumn<InterfaceBind, String> column1_apiInterClass;
	@FXML public TableColumn<InterfaceBind, String> column2_clientClass;
	@FXML public Tooltip tt_clientjarpath;
	@FXML public Tooltip tt_apijarpath;
	@FXML public AnchorPane pane;

	public void setMain(ManualMapper main, ApiData data) {
		this.main = main;
		this.data = data;
		postInit();
	}

	private void postInit() {
		// throws NPE if in initialize() since 'main' will be null
		createContextMenuBrowseForJar(select_jar_api, main.recentFiles, true, label_path_1);
		createContextMenuBrowseForJar(select_jar_client, main.recentFiles2, false, label_path_2);
	}

	private ManualMapper main;
	private ApiData data;

	@FXML
	void onAction(ActionEvent e) {
		EventTarget target = e.getTarget();
		assert target != null;

		if (target == load_jar_api) {
			if (data.pbApi == null) {
				label_path_1.setText("No Api JAR selected!");
			} else {
				list1.setItems(FXCollections.observableArrayList(data.pbApi.interfaces));
				StringBuilder sb = new StringBuilder();
				sb.append("Loaded at: "+data.pbApi.apiJar.getAbsolutePath());
				sb.append("\nAccessor count: "+data.pbApi.interfaces.size());
				data.pbApi.interfaces.forEach(i -> {
					sb.append("\n\t"+i.name+" has "+i.fields.size()+" fields");
				});
				tt_apijarpath.setText(sb.toString());
				Notifications.create().title("Parabot Mapper").text("Reloaded API JAR-" + new Random().nextInt(100)).show();
				onLoadJar();
			}
		}
		else if (target == load_jar_client) {
			if (data.client == null) {
				label_path_2.setText("No Client JAR selected!");
			} else {
				list2.setItems(FXCollections.observableArrayList(data.client.entries));
				tt_clientjarpath.setText("Loaded at: "+data.client.clientFile.getAbsolutePath()+"\nClass count: "+data.client.entries.size());
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
		ClientClass c = list2.getSelectionModel().getSelectedItem();
		if (x == null || c == null) {
			System.err.println("A selected Accessor or Client Class is null");
			Notifications.create().title("Error").text(String.format("You need to select an %s",
			x == null ? "API Interface class" : "Client class")).showError();
		}
		else if (table1.getItems().stream().filter(e -> e.getApiClass().equals(x.name) || e.getClientClass().equals(c.name)).count() > 0) {
			final String mes = String.format(
					"Either Accessor %s or Client class %s is already bound. Either remove this binding or " +
							"choose a new one", x.name, c.name);
			System.err.println(mes);
			Notifications.create().title("Error").text(mes).showWarning();
		}
		else {
			table1.getItems().add(new InterfaceBind(x, c));
			System.out.println("added new PbLink. Total now "+table1.getItems().size()+". Columns: "+table1.getColumns().size());
			main.tab2().unlockPanel();
		}
	}

	public void onTabOpened() {
		main.resizeTo(pane.getPrefWidth() + 15, pane.getPrefHeight() + 60);
	}

	public static class InterfaceBind {
		private final SimpleStringProperty apiClass;
		private final SimpleStringProperty clientClass;

		public InterfaceBind(ApiInterface apiInterface, ClientClass clientClass) {
			apiClass = new SimpleStringProperty(apiInterface.name);
			this.clientClass = new SimpleStringProperty(clientClass.name);
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
		System.out.println("initializing...");

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
						super.updateItem(item, empty);

						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							setText(item.name);
						}
					}
				};
			}
		});

		list2.setCellFactory(new Callback<>() {
			@Override
			public ListCell<ClientClass> call(ListView<ClientClass> param) {
				return new ListCell<>() {
					@Override
					protected void updateItem(ClientClass item, boolean empty) {
						super.updateItem(item, empty);

						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							setText(item.name);
						}
					}
				};
			}
		});

		// We're ready
		System.out.println("ready");
	}

	private void createContextMenuBrowseForJar(Button button, List<Path> recentFiles, boolean api, Label label) {
		button.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			ContextMenu menu = Optional.ofNullable(button.getContextMenu()).orElseGet(() -> {
				ContextMenu m = new ContextMenu();
				button.setContextMenu(m);
				return m;
			});
			List<MenuItem> items = new ArrayList<>(0);
			recentFiles.stream()
					.map(Path::toFile)
					.forEach(f -> {
						MenuItem menuItem = new MenuItem(f.getAbsolutePath());
						menuItem.setOnAction(event1 -> {
							if (api) {
								data.pbApi = new PbApi(data, f);
							} else {
								data.client = new RspsClient(f);
							}
							label.setText(f.getAbsolutePath());
							Notifications.create().title("Parabot Mapper").text("Loaded API JAR at "+f.getAbsolutePath()).show();
						});
						items.add(menuItem);
					});
			MenuItem fileBrowse = new MenuItem("Browse for Jar");
			fileBrowse.setOnAction(event12 -> {
				FileChooser fc = new FileChooser();
				fc.setTitle("Find API JAR");
				fc.setInitialDirectory(new File(System.getProperty("user.home")));
				fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR", "*.jar"));
				File f = fc.showOpenDialog(main.getStage());
				if (f != null && f.getPath().endsWith(".jar")) {
					if (api) {
						data.pbApi = new PbApi(data, f);
					} else {
						data.client = new RspsClient(f);
					}
					label.setText(f.getAbsolutePath());
					Notifications.create().title("Parabot Mapper").text("Loaded "+(api?"API":"Client")+" JAR at "+f.getAbsolutePath()).show();
					if (!recentFiles.contains(f.toPath()))
						recentFiles.add(f.toPath());
				}
			});
			items.add(fileBrowse);

			menu.getItems().clear();
			menu.getItems().addAll(items);
			menu.show(button, event.getScreenX(), event.getScreenY());
		});
	}

	private void createContextMenuCellFactory(TableColumn<InterfaceBind, String> col) {
		final StringProperty contextMenuValue = new SimpleStringProperty("");
		final ContextMenu menu = new ContextMenu();
		final MenuItem mi = new MenuItem();
		// text depends on "current cell"
		// more commonly, the action handler would depend on this value,
		// but this is just a proof of concept
		mi.textProperty().bind(Bindings.format("Unbind %s", contextMenuValue));
		mi.setOnAction(event -> {
			InterfaceBind ib = table1.getSelectionModel().getSelectedItem();
			table1.getItems().remove(ib);
			Notifications.create().title("Parabot Mapper")
					.text(String.format("Unbound [%s : %s]", ib.getApiClass(), ib.getClientClass())).show();
		});
		menu.getItems().addAll(mi);

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
