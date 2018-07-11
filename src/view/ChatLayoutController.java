package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ChatIcon;
import model.Friend;
import controller.WebServer;
import javafx.fxml.Initializable;
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
    private Button btn1;

    private Stage nowStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn1.setOnAction((MouseEvent)->{
           btn1.setText("clicked");
        });




    }

    public void setButton(ChatIcon btn, int position){
        btn.setLayoutX(centerX - radius*Math.sin(Math.toRadians(position)));
        btn.setLayoutY(centerY - radius*Math.cos(Math.toRadians(position)));
        /*
        chatPane.setTopAnchor(btn, centerY - radius*Math.cos(Math.toRadians(position)));
        chatPane.setBottomAnchor(btn, centerY + radius*Math.cos(Math.toRadians(position)));
        chatPane.setLeftAnchor(btn, centerX - radius*Math.sin(Math.toRadians(position)));
        chatPane.setRightAnchor(btn, centerX + radius*Math.sin(Math.toRadians(position)));
        */
        chatPane.getChildren().add(btn);
    }


    public void setStage(Stage stage) {
        this.nowStage = stage;
    }
}
