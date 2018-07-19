package model;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import view.ChatLayoutController;

public class ChatIcon extends Button {
   public Friend friend;

   ChatRoom myChatRoom;

   double positionX;
   double positionY;


   private ChatLayoutController controller;
   private Stage stage = new Stage();


    TextArea chatTextArea = new TextArea();

   /*
    public ChatIcon(Friend friend, ChatRoom room, String name){
       super(name);
       this.friend = friend;
        this.myChatRoom = room;
       this.setId(friend.getId());
       this.setMinSize(30,30);
       this.setMaxSize(30,30);
       this.setPrefSize(30,30);
      // this.setGraphic(new ImageView(img));
   }

   */
   public ChatIcon(Friend friend, ChatRoom room){
       super();
       this.friend = friend;
       this.myChatRoom = room;
       stage = room.getStage();
       controller = room.getController();
       this.setId(friend.getId());
       this.setMinSize(30,30);
       this.setMaxSize(30,30);
       this.setPrefSize(30,30);

   }

   TextArea getChatTextArea(String message){
       chatTextArea.setPrefHeight(80);
       chatTextArea.setPrefWidth(100);
       chatTextArea.setText(message);
       chatTextArea.setWrapText(true);
       chatTextArea.setLayoutX(positionX-50);
       chatTextArea.setLayoutY(positionY-90);

       return chatTextArea;
   }
   TextArea getChatTextArea(){
        chatTextArea.setPrefHeight(80);
        chatTextArea.setPrefWidth(100);
        chatTextArea.setWrapText(true);
        chatTextArea.setLayoutX(positionX-50);
        chatTextArea.setLayoutY(positionY-90);

        return chatTextArea;
    }

   void setMessage(String message){
       chatTextArea.setText(message);
   }


   String getFriendId(){
       return friend.getId();
   }



   void setPosition(double x, double y){
       this.positionX = x;
       this.positionY = y;

       this.setLayoutX(this.positionX);
       this.setLayoutY(this.positionY);
   }

   public String getMemberId(){
       return friend.getId();
   }



}
