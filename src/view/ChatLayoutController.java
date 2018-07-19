package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.ChatIcon;
import model.ChatRoom;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatLayoutController implements Initializable {

    int centerX = 218;
    int centerY = 185;

    int radius = 157;

    @FXML
    private AnchorPane chatPane;

    @FXML
    private AnchorPane historyPane;

    @FXML
    private Button btnInvite;

    @FXML
    private Button btnSend;

    @FXML
    private TextField textFieldMessage;

    private Stage nowStage;
    private ChatRoom chatRoom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnInvite.setOnAction((MouseEvent)->{

           btnInvite.setText("clicked");
        });

        btnSend.setOnAction ((MouseEvent)->{

            try {
                chatRoom.setLastMessage(textFieldMessage.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });




    }

    public void setButton(ChatIcon btn){
       /*
        chatPane.setTopAnchor(btn, centerY - radius*Math.cos(Math.toRadians(position)));
        chatPane.setBottomAnchor(btn, centerY + radius*Math.cos(Math.toRadians(position)));
        chatPane.setLeftAnchor(btn, centerX - radius*Math.sin(Math.toRadians(position)));
        chatPane.setRightAnchor(btn, centerX + radius*Math.sin(Math.toRadians(position)));
        */
        chatPane.getChildren().add(btn);
    }

    public void setTextArea(TextArea textArea){
        chatPane.getChildren().add(textArea);
    }

    public void setChatRoom(ChatRoom room){
        this.chatRoom = room;
    }


    public void setStage(Stage stage) {
        this.nowStage = stage;
    }
}
