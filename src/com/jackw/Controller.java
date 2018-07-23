package com.jackw;

import com.jackw.logic.ApiData;
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

public class Controller {

	@FXML private Label lblApiPath;
	@FXML private Button btnLoadApiJar;
	@FXML private Button btnReloadApiJar;
	@FXML private ListView<ListEntryAdapter<ApiData.ApiInterface>> list1;
	@FXML private ListView<ApiData.ClientEntry> list2;
	@FXML private TableView<PbLink> table1;
	@FXML MenuItem menuitem_link_api_interface;
	@FXML MenuItem menuitem_link_class;
	@FXML TableColumn<PbLink, String> apiClass;
	@FXML TableColumn<PbLink, String> clientClass;

	private ApiData data;

	@FXML
	void onAction(ActionEvent e) {
		EventTarget target = e.getTarget();
		assert target != null;
		if (target == btnLoadApiJar) {
			data = new ApiData();
			lblApiPath.setText(data.pbApi.apiJar.getAbsolutePath());
			List<ListEntryAdapter<ApiData.ApiInterface>> list = data.pbApi.interfaces.stream()
					.map(i -> new ListEntryAdapter<>(i.getClass().getName(), i))
					.collect(Collectors.toList());
			list1.setItems(FXCollections.observableArrayList(list));
			list2.setItems(FXCollections.observableArrayList(data.client.entries));
			Notifications.create().title("Parabot Mapper").text("Loaded API JAR").show();
		}
		else if (target == btnReloadApiJar) {
			if (data == null) {
				lblApiPath.setText("No Api JAR selected!");
			} else {
				lblApiPath.setText("aye: " + new Random().nextInt(100));
				Notifications.create().title("Parabot Mapper").text("Reloaded API JAR").show();
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
		ListEntryAdapter<ApiData.ApiInterface> x = list1.getSelectionModel().getSelectedItem();
		ApiData.ClientEntry c = list2.getSelectionModel().getSelectedItem();
		if (x == null || c == null) {
			System.out.println("bad selection");
			Notifications.create().title("Error").text(String.format("You need to select an %s",
			x == null ? "API Interface class" : "Client class")).showError();
		} else {
			table1.getItems().add(new PbLink(x.toString(), c.name));
			System.out.println("added new PbLink. Total now "+table1.getItems().size());
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
			public ListCell<ListEntryAdapter<ApiData.ApiInterface>> call(ListView<ListEntryAdapter<ApiData.ApiInterface>> param) {
				return new ListCell<>() {
					@Override
					protected void updateItem(ListEntryAdapter<ApiData.ApiInterface> item, boolean empty) {
						if (item == null) return;
						super.updateItem(item, empty);
						setText(item.getO().getClass().getSimpleName());
					}
				};
			}
		});
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
