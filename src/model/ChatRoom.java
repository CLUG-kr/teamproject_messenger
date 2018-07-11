package model;

import controller.WebServer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.ChatLayoutController;
import view.*;

import java.io.IOException;

public class ChatRoom {
    private String chatRoomId;
    private Friend member[] = new Friend[100];
    private ChatIcon icon[] = new ChatIcon[100];

    private ChatLayoutController controller;
    private Stage stage = new Stage();

    private int nowMemberCount = 3;

    private WebServer server;

    public ChatRoom(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public void readHistory() {

    }

    public void showChat() {
        stage.showAndWait();
    }

    public void linkMember() {

    }

    public void refreshLayout(){


    }
    public void makeLayout(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/ChatLayout.fxml"));
            Scene scene = new Scene(loader.load());
            controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("채팅방");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < nowMemberCount; i++) {
            ChatIcon btn = new ChatIcon(new Friend(), "K");
            controller.setButton(btn, 360/nowMemberCount*i);
        }
    }

    public void serverInitialize() {
        server = new WebServer(chatRoomId);
        if (server.memberIsAvailable()) {
            member[nowMemberCount] = server.member();
            icon[nowMemberCount] = new ChatIcon(member[nowMemberCount]);
        }
    }

    public void initialize(){
        makeLayout();
        showChat();
    }



}
