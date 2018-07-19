package model;

import controller.WebServer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import view.ChatLayoutController;

import java.io.IOException;
import java.net.MalformedURLException;

public class ChatRoom extends Thread{
    private StringProperty chatRoomId;
    private StringProperty chatRoomName;

    private Friend member[] = new Friend[100];
    private ChatIcon icon[] = new ChatIcon[100];

    int centerX = 437;
    int centerY = 387;

    int radius = 270;

    String userId;

    private ChatLayoutController controller;
    private Stage stage = new Stage();

    String title;

    private int nowMemberCount = 3;

    private WebServer server;
    private String lastMessage;


    public ChatRoom(String chatRoomName) {
        this.chatRoomName = new SimpleStringProperty(chatRoomName);
    }

    public ChatRoom() {

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
            controller.setChatRoom(this);
            stage.setTitle(chatRoomName.get());
//            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < nowMemberCount; i++) {
            controller.setButton(icon[i]);
            controller.setTextArea(icon[i].getChatTextArea(icon[i].getFriendId()));
        }

    }

    public void serverInitialize() throws IOException {

        int tempCount = 0;
        int position;
        try {
            server = new WebServer();

            server.setUserId(userId);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(chatRoomId == null){
            chatRoomId = new SimpleStringProperty(server.makeChatRoomId(chatRoomName.get()));
            System.out.println(chatRoomName.get());
        }
        server.setChatRoomId(chatRoomId.get());
        server.chatRoomInitialize();
        nowMemberCount = server.getMemberCount();
        position = 360/nowMemberCount;
        while (server.memberIsAvailable()){
            member[tempCount] = server.member();
            icon[tempCount] = new ChatIcon(member[tempCount],this);
            icon[tempCount].setPosition(centerX - radius*Math.sin(Math.toRadians(position*tempCount)), centerY - radius*Math.cos(Math.toRadians(position*tempCount)));
            tempCount++;
        }



    }

    public void initialize(String myId) throws IOException {
        this.userId = myId;
        serverInitialize();
        makeLayout();

        Thread readerThread = new Thread(new Reader());
        readerThread.start();
        showChat();

    }



    public void setChatRoomId(String roomId){
        chatRoomId = new SimpleStringProperty(roomId);
    }

    public String getChatRoomName(){
        return chatRoomName.get();
    }

    public Stage getStage(){
        return stage;
    }

    public ChatLayoutController getController() {
        return controller;
    }

    public int findMember(String memberId){
        for(int i = 0; i < nowMemberCount;i++){
            if(icon[i].getMemberId().equals(memberId)){
                return i;
            }
        }
        return -1;
    }

    public void setLastMessage(String text) throws IOException {

        server.sendMessage(text);
    }


    public class Reader implements Runnable {


        @Override
        public void run() {
            server.chatInitialize();
            while (true) {

                String[] chatInfo = new String[0];
                try {
                    chatInfo = server.getChat().split("`");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("text" + chatInfo[1]);
                icon[findMember(chatInfo[0])].setMessage(chatInfo[1]);
              //  controller.setTextArea(icon[findMember(chatInfo[0])].getChatTextArea());


            }
        }
    }
}
