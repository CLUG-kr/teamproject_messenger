package view;

import application.TableRowDataModel;
import controller.MyServer;
import controller.WebServer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.ChatRoom;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{


	@FXML
	private TableView<ChatRoom> myTableView;
	@FXML
	private TableColumn<ChatRoom, String> title;
	@FXML
	TextField titleInput;
	@FXML
	private Button addButton;
	@FXML
	private Button join;

	Stage stage;

	String myId;

	MyServer myServer;
	WebServer server;
	ObservableList<TableRowDataModel> myList = FXCollections.observableArrayList(
			new TableRowDataModel(new SimpleStringProperty("Name 1")),
			new TableRowDataModel(new SimpleStringProperty("Name 2"))
	);

	public MainController() throws IOException {
	}

	public void Join(ActionEvent event) throws IOException {
		myTableView.getSelectionModel().getSelectedItem();
		myTableView.getSelectionModel().getSelectedItem().initialize(myId);
	}

	@Override
	public void initialize(URL location,ResourceBundle resources) {

		title.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getChatRoomName()));


		/*
		try {
			server.Login("aiel","aiel");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}


		while(myServer.isAvailable()){
			ChatRoom chatRoom = new ChatRoom(myServer.getRoomName());
			chatRoom.setChatRoomId(myServer.getRoomId());
			myTableView.getItems().add(chatRoom);
		}
*/

		addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ChatRoom chatRoom = new ChatRoom(titleInput.getText());
				myTableView.getItems().add(chatRoom);
			}

		});
/*
		myTableView.getSelectionModel().selectedItemProperty().addListener(

		);*/
	}

	void start(){

	}

	public void setStage(Stage stage){
		this.stage = stage;
	}

	public void setId(String text) {
		this.myId = text;
	}

	public void setServer(WebServer server) {
		this.server = server;
	}

	public void getRoom() throws IOException, ParseException {
		myServer = new MyServer(myId);
		myServer.initialize();
		while(myServer.isAvailable()){
			ChatRoom chatRoom = new ChatRoom(myServer.getRoomName());
			chatRoom.setChatRoomId(myServer.getRoomId());
			myTableView.getItems().add(chatRoom);
		}
	}
}

