package view;
 
import controller.WebServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;

public class LoginController {
 
    @FXML
    private Label lblStatus;
    
    @FXML
    private TextField txtUserName;
    
    @FXML
    private TextField txtPassword;

    private WebServer server= new WebServer();

    public LoginController() throws IOException {
    }

    public void Login(ActionEvent event) throws Exception{
        if(server.Login(txtUserName.getText(), txtPassword.getText())){
            lblStatus.setText("Login Success");

            Stage primaryStage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainLayout.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            MainController controller = loader.getController();
            controller.setId(txtUserName.getText());
            controller.setServer(server);
            controller.getRoom();
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.setTitle("Messenger");
            primaryStage.setScene(scene);
            primaryStage.show();



        }else{
            lblStatus.setText("Login Failed");
        }
    }
    public void Register(ActionEvent event) throws Exception{
        if(server.Register(txtUserName.getText(), txtPassword.getText())){
            lblStatus.setText("Register Success");




        }else{
            lblStatus.setText("Register Failed");
        }
    }
}

