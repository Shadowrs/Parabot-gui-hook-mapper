package com.jackw.controllers;

import com.jackw.ManualMapper;
import com.jackw.model.dummyapi.ApiInterface;
import com.jackw.model.dummyapi.ClientClass;
import com.jackw.model.dummyapi.JavaField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.controlsfx.control.Notifications;
import org.parabot.api.output.Logger;

public class Tab2Controller {

	@FXML public Label label_steps_fixed;
	@FXML public Label label_locked;
	@FXML public AnchorPane rootpane;
	@FXML public ComboBox<ApiInterface> box_accessor;
	@FXML public ComboBox<JavaField> box_api_methods;
	@FXML public ComboBox<JavaField> box_client_fields_typed;
	@FXML public ComboBox<JavaField> box_client_all_fields;
	@FXML public TextField text_client_class;
	@FXML public Button button_bind_getter;
	@FXML public Label label_api_fields;
	@FXML public Label label_client_fields_typed;
	@FXML public Label label_all_client_fields;
	@FXML public CheckBox tickbox_static;
	@FXML public Button button_setter;
	@FXML public AnchorPane tab1pane;
	@FXML public TableView<GetterHook> table1;
	@FXML public TableColumn<GetterHook, String> table1_col1;
	@FXML public TableColumn<GetterHook, String> table1_col2;
	@FXML public TableColumn<GetterHook, String> table1_col3;
	@FXML public TableColumn<GetterHook, String> table1_col4;

	@FXML public TableView<SetterHook> table2;
	@FXML public TableColumn<SetterHook, String> table2_col1;
	@FXML public TableColumn<SetterHook, String> table2_col2;
	@FXML public TableColumn<SetterHook, String> table2_col3;
	@FXML public TableColumn<SetterHook, String> table2_col4;
	@FXML public TableColumn<?, ?> col_api;
	@FXML public TableColumn<?, ?> col_client;
	@FXML public TableColumn<?, ?> col_api2;
	@FXML public TableColumn<?, ?> col_client2;

	public void setMain(ManualMapper main) {
		this.main = main;
		postInit();
	}

	private void postInit() {
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
				if (object == null) return "";
				return object.name;
			}

			@Override
			public ApiInterface fromString(String string) {
				return box_accessor.getItems().filtered(apiInterface -> apiInterface.name.equals(string)).get(0);
			}
		});

		box_api_methods.setConverter(new StringConverter<>() {
			@Override
			public String toString(JavaField object) {
				if (object == null) return "";
				return object.getDisplayForASMType();
			}

			@Override
			public JavaField fromString(String string) {
				System.out.println("when "+string);
				return box_api_methods.getItems().filtered(f -> f.name.equals(string)).get(0);
			}
		});

		box_client_fields_typed.setConverter(new StringConverter<>() {
			@Override
			public String toString(JavaField object) {
				if (object == null) return "";
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
				if (object == null) return "";
				return object.getDisplayForASMType();
			}

			@Override
			public JavaField fromString(String string) {
				return box_client_all_fields.getItems().filtered(f -> f.name.equals(string)).get(0);
			}
		});

		box_accessor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue == null && newValue == null) return; // no change..
			if (newValue == null) {
				current_assoc_class = null;
				cascadeClearSelections(box_accessor);
				//System.err.println("how does this happen "+oldValue.name);
				return;
			}
			box_api_methods.setDisable(false);
			ObservableList<JavaField> list = FXCollections.observableArrayList(newValue.getMethods());
			box_api_methods.setItems(list);
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
				Logger.warning("Tab2Controller", "Setting inner Items  - but count is "+list.size()+" -- this can be slow!");
			}

			box_client_all_fields.setItems(list);

			if (list.size() > 100)
				Logger.warning("Tab2Controller", "Item list set in "+(System.currentTimeMillis()-s)+"ms");

			text_client_class.setText(ib.getClientClass());

			label_all_client_fields.setText("3. Alternative: All "+text_client_class.getText()+" Fields ("+
					box_client_all_fields.getItems().size()+") :");
		});

		box_api_methods.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue == null && newValue == null) return; // no change..
			if (newValue == null) {
				cascadeClearSelections(box_api_methods);
				return;
			}
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
			if (oldValue == null && newValue == null) return; // no change..
			// Special case: this has a 'brother' box - cleared when this value is set to NotNull
			if (newValue != null)
				cascadeClearSelections(box_client_fields_typed);
		});

		box_client_all_fields.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue == null && newValue == null) return; // no change..
			// Special case: this has a 'brother' box - cleared when this value is set to NotNull
			if (newValue != null)
				cascadeClearSelections(box_client_all_fields);
		});


		table1_col1.setCellValueFactory(new PropertyValueFactory<>("accessor"));
		table2_col1.setCellValueFactory(new PropertyValueFactory<>("accessor"));
		table1_col2.setCellValueFactory(new PropertyValueFactory<>("method"));
		table2_col2.setCellValueFactory(new PropertyValueFactory<>("method"));
		table1_col3.setCellValueFactory(new PropertyValueFactory<>("clientClass"));
		table2_col3.setCellValueFactory(new PropertyValueFactory<>("clientClass"));
		table1_col4.setCellValueFactory(new PropertyValueFactory<>("javaField"));
		table2_col4.setCellValueFactory(new PropertyValueFactory<>("javaField"));

		Arrays.asList(table1_col1, table1_col2, table1_col3, table1_col4).forEach(c ->
				createContextMenuCellFactory(c,
						getterHook -> {
							table1.getItems().remove(getterHook);
							Notifications.create().title("Parabot Mapper")
									.text(String.format("Unbound %s : %s -> %s : %s",
											getterHook.accessor.get(),
											getterHook.method.get(),
											getterHook.clientClass.get(),
											getterHook.javaField.get()
									))
									.show();
						}, table1.getSelectionModel()));

		Arrays.asList(table2_col1, table2_col2, table2_col3, table2_col4).forEach(c -> createContextMenuCellFactory(c,
				setterHook -> {
					table2.getItems().remove(setterHook);
					Notifications.create().title("Parabot Mapper")
							.text(String.format("Unbound [%s : %s -> %s : %s]",
									setterHook.accessor.get(),
									setterHook.method.get(),
									setterHook.clientClass.get(),
									setterHook.javaField.get()
									)
							).show();
				}, table2.getSelectionModel()));

		tab1pane.widthProperty().addListener((observable, oldValue, newValue) -> {
			col_api.setPrefWidth(newValue.doubleValue() / 2);
			col_client.setPrefWidth((newValue.doubleValue() / 2) - 3);
			col_api2.setPrefWidth(newValue.doubleValue() / 2);
			col_client2.setPrefWidth((newValue.doubleValue() / 2) - 3); // far right side
			table1_col1.setPrefWidth(newValue.doubleValue() / 4);
			table1_col2.setPrefWidth(newValue.doubleValue() / 4);
			table1_col3.setPrefWidth(newValue.doubleValue() / 4);
			table1_col4.setPrefWidth((newValue.doubleValue() / 4) - 6); // far right side
			table2_col1.setPrefWidth(newValue.doubleValue() / 4);
			table2_col2.setPrefWidth(newValue.doubleValue() / 4);
			table2_col3.setPrefWidth(newValue.doubleValue() / 4);
			table2_col4.setPrefWidth((newValue.doubleValue() / 4) - 6);
		});

		System.out.println("[Tab2 Controller] Init complete");

	}

	private <T> void createContextMenuCellFactory(final TableColumn<T, String> col, final Consumer<T> consumer,
												  final TableView.TableViewSelectionModel<T> model) {
		final StringProperty contextMenuValue = new SimpleStringProperty("");
		final ContextMenu menu = new ContextMenu();
		final MenuItem mi = new MenuItem();
		mi.textProperty().bind(Bindings.format("Unbind %s", contextMenuValue));
		mi.setOnAction(event -> consumer.accept((T) model.getSelectedItem()));
		menu.getItems().addAll(mi);

		col.setCellFactory(param -> {
			TableCell<?, String> cell = new TableCell<>() {
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
			return (TableCell<T, String>) cell;
		});
	}

	private void cascadeClearSelections(Node node) {
		// multiple changes
		if (Arrays.stream(new Node[]{box_api_methods, box_accessor}).anyMatch(n -> n == node)) {
			box_client_all_fields.getSelectionModel().clearSelection();
			box_client_fields_typed.getSelectionModel().clearSelection();
		}
		if (node == box_accessor) { // single change
			box_api_methods.getSelectionModel().clearSelection();
			text_client_class.setText("?");
		}
		if (node == box_client_all_fields) {
			box_client_fields_typed.getSelectionModel().clearSelection();
		}
		if (node == box_client_fields_typed) {
			box_client_all_fields.getSelectionModel().clearSelection();
		}
	}

	public void unlockPanel() {
		label_locked.setVisible(false); // hide warning
		rootpane.getChildren().stream().filter(c -> c != label_locked).forEach(c -> c.setOpacity(1));
		rootpane.getChildren().stream().filter(c -> !Arrays.asList(box_client_all_fields, box_client_fields_typed, box_api_methods).contains(c)).forEach(c -> c.setDisable(false));

	}

	public void lockPanel() {
		label_locked.setVisible(true); // hide warning
		rootpane.getChildren().stream().filter(c -> c != label_locked).forEach(c -> {
			c.setOpacity(0.15);
			c.setDisable(true);
		});
	}

	@FXML void onAction(ActionEvent e) {
		if (e.getTarget() == button_bind_getter || e.getTarget() == button_setter) {
			JavaField apiField = box_api_methods.getSelectionModel().getSelectedItem();
			if (apiField == null) {
				Notifications.create().text("You need to select an API Field to bind!").showWarning();
				return;
			}
			JavaField clientField = box_client_all_fields.getSelectionModel().getSelectedItem() != null ?
			box_client_all_fields.getSelectionModel().getSelectedItem() :
			box_client_fields_typed.getSelectionModel().getSelectedItem();
			if (clientField == null) {
				Notifications.create().text("You need to select a Client Field to bind!").showWarning();
				return;
			}
			if (e.getTarget() == button_setter) {
				table2.getItems().add(new SetterHook(box_accessor.getSelectionModel().getSelectedItem(),
						apiField,
						current_assoc_class,
						clientField));
			} else {
				table1.getItems().add(new GetterHook(box_accessor.getSelectionModel().getSelectedItem(),
						apiField,
						current_assoc_class,
						clientField));
			}
			System.out.println(String.format("Accessor: %s.%s -> %s.%s", box_accessor.getSelectionModel().getSelectedItem().name,
					apiField.name, current_assoc_class.name, clientField.name));
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

	public void updateAccessorsListItems() {
		// Map The Column cells in Tab1 Table1 (String: Accessor names) to actual accessor instances in list1.
		List<ApiInterface> items = main.tab1().table1.getItems().stream()
				.map(Tab1Controller.InterfaceBind::getApiClass)
				.map(e -> main.data.pbApi.interfaces.stream()
						.filter(i -> i.name.equals(e)).findFirst().get())
				.collect(Collectors.toCollection(() -> new ArrayList<>(0)));
		box_accessor.getSelectionModel().clearSelection(); // otherwise NPE thrown by onSelectionChanged() due to null new val
		box_accessor.setItems(FXCollections.observableArrayList(items));
		box_api_methods.getSelectionModel().clearSelection();
		box_api_methods.setItems(FXCollections.observableArrayList());
		box_client_fields_typed.getSelectionModel().clearSelection();
		box_client_fields_typed.setItems(FXCollections.observableArrayList());
		box_client_all_fields.getSelectionModel().clearSelection();
		box_client_all_fields.setItems(FXCollections.observableArrayList());
		text_client_class.setText("?");
		label_api_fields.setText("2. Available API methods:");
		label_client_fields_typed.setText("3. Available Client Fields by Type:");
		label_all_client_fields.setText("3. Alternative: All Client Class Fields:");
	}

	public static class GetterHook {
		public String getAccessor() {
			return accessor.get();
		}

		public SimpleStringProperty accessorProperty() {
			return accessor;
		}

		public String getMethod() {
			return method.get();
		}

		public SimpleStringProperty methodProperty() {
			return method;
		}

		public String getClientClass() {
			return clientClass.get();
		}

		public SimpleStringProperty clientClassProperty() {
			return clientClass;
		}

		public String getJavaField() {
			return javaField.get();
		}

		public SimpleStringProperty javaFieldProperty() {
			return javaField;
		}

		public final SimpleStringProperty accessor;
		public final SimpleStringProperty method;
		public final SimpleStringProperty clientClass;
		public final SimpleStringProperty javaField;

		public GetterHook(ApiInterface accessor, JavaField accessorMethod, ClientClass clientClass, JavaField clientField) {
			this.accessor = new SimpleStringProperty(accessor.name);
			this.method = new SimpleStringProperty(accessorMethod.name);
			this.clientClass = new SimpleStringProperty(clientClass.name);
			this.javaField = new SimpleStringProperty(clientField.name);
		}
	}

	public static class SetterHook {
		public String getAccessor() {
			return accessor.get();
		}

		public SimpleStringProperty accessorProperty() {
			return accessor;
		}

		public String getMethod() {
			return method.get();
		}

		public SimpleStringProperty methodProperty() {
			return method;
		}

		public String getClientClass() {
			return clientClass.get();
		}

		public SimpleStringProperty clientClassProperty() {
			return clientClass;
		}

		public String getJavaField() {
			return javaField.get();
		}

		public SimpleStringProperty javaFieldProperty() {
			return javaField;
		}

		public final SimpleStringProperty accessor;
		public final SimpleStringProperty method;
		public final SimpleStringProperty clientClass;
		public final SimpleStringProperty javaField;

		public SetterHook(ApiInterface accessor, JavaField accessorMethod, ClientClass clientClass, JavaField clientField) {
			this.accessor = new SimpleStringProperty(accessor.name);
			this.method = new SimpleStringProperty(accessorMethod.name);
			this.clientClass = new SimpleStringProperty(clientClass.name);
			this.javaField = new SimpleStringProperty(clientField.name);
		}
	}
}
