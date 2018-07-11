package view;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ChatRoom;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class RootLayoutController  implements Initializable{
    private Stage primaryStage;
    @FXML
    private Button btn1;

    private ChatRoom room = new ChatRoom("testCode");
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        btn1.setOnAction((MouseEvent)->{
            showChatRoom();
        });


    }

    void showChatRoom(){
        room.initialize();
    }




}
